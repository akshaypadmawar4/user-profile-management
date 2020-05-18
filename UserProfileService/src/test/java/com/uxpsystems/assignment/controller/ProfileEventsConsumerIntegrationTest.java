package com.uxpsystems.assignment.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.dao.ProfileRepository;
import com.uxpsystems.assignment.entity.ProfileEvent;
import com.uxpsystems.assignment.entity.ProfileEventType;
import com.uxpsystems.assignment.entity.UserProfile;

@SpringBootTest
@EmbeddedKafka(topics = {"library-events"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"
, "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class ProfileEventsConsumerIntegrationTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    KafkaListenerEndpointRegistry endpointRegistry;

    @Autowired
    ProfileRepository profileEventsRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        for (MessageListenerContainer messageListenerContainer : endpointRegistry.getListenerContainers()){
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }


    @Test
    void publishUpdateProfileEvent() throws JsonProcessingException, ExecutionException, InterruptedException {
        
        UserProfile userProfile = new UserProfile(); 
        userProfile.setAddress("Hinjewadi");
        userProfile.setPhoneNumber(9028380486L);
       
        UserProfile userProfileSaved= profileEventsRepository.save(userProfile);
        System.out.println(userProfileSaved);
        userProfileSaved.setAddress("Baner");
        //publish the update ProfileEvent

        ProfileEvent profileEvent = new ProfileEvent();
        profileEvent.setProfileId(userProfileSaved.getId());
        profileEvent.setProfileEventType(ProfileEventType.UPDATE);
        profileEvent.setProfile(userProfileSaved);
        
        String updatedJson = objectMapper.writeValueAsString(profileEvent);
        kafkaTemplate.sendDefault(profileEvent.getProfileId(), updatedJson).get();

        
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);
        
        UserProfile persistedProfileEvent = profileEventsRepository.findById(profileEvent.getProfileId()).get();
        
        assertEquals("Baner", persistedProfileEvent.getAddress());
        
        profileEventsRepository.delete(persistedProfileEvent);
        
        
    }
    
    @Test
    void publishDeleteProfileEvent() throws JsonProcessingException, ExecutionException, InterruptedException {
        
        UserProfile userProfile = new UserProfile(); 
        userProfile.setAddress("Hinjewadi");
        userProfile.setPhoneNumber(9028380486L);
       
        UserProfile userProfileSaved= profileEventsRepository.save(userProfile);
        System.out.println(userProfileSaved);

        ProfileEvent profileEvent = new ProfileEvent();
        profileEvent.setProfileId(userProfileSaved.getId());
        profileEvent.setProfileEventType(ProfileEventType.DELETE);
        
      //publish the delete ProfileEvent
        String updatedJson = objectMapper.writeValueAsString(profileEvent);
        kafkaTemplate.sendDefault(profileEvent.getProfileId(), updatedJson).get();

        
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);
        
        Optional<UserProfile> persistedProfileEvent = profileEventsRepository.findById(profileEvent.getProfileId());
        
        assertTrue(!persistedProfileEvent.isPresent());
        
        
    }
}
