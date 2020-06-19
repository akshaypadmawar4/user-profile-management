package com.uxpsystems.assignment.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.dao.ProfileRepository;
import com.uxpsystems.assignment.entity.ProfileEvent;
import com.uxpsystems.assignment.entity.ProfileEventType;
import com.uxpsystems.assignment.entity.UserProfile;

@ExtendWith(MockitoExtension.class)
public class ProfileEventServiceTest {
	
	@InjectMocks
	@Spy
	ProfileEventsService profileEventService;
	
	@Mock
	ObjectMapper objectMapper;
	
	@Mock
	ProfileRepository profileRepository;
	
	
	@Test
    public void processProfileEventUpdateTest() throws JsonProcessingException
    {
		
		UserProfile profile = new UserProfile();
        profile.setId(1);
        profile.setAddress("Ravet");
        profile.setPhoneNumber(9028380486L);
        
        ProfileEvent profileEvent = new ProfileEvent();
        profileEvent.setProfileId(1);
        profileEvent.setProfileEventType(ProfileEventType.UPDATE);
        profileEvent.setProfile(profile);
		
		when(objectMapper.readValue("Consumer",ProfileEvent.class)).thenReturn(profileEvent);
        
        when(profileRepository.findById(profileEvent.getProfileId())).thenReturn(Optional.of(profile));
        
        when(profileRepository.save(profile)).thenReturn(profile);
        
        ConsumerRecord<Integer,String> consumerRecord = new ConsumerRecord<Integer, String>("profile-event", 0, 0, 0, null, 0, 0, 0, null, "Consumer");
        
        profileEventService.processProfileEvent(consumerRecord);
        
        verify(profileEventService).update(profileEvent);
      
    }
	
	@Test
    public void processProfileEventDeleteTest() throws JsonProcessingException
    {
		
		UserProfile profile = new UserProfile();
        profile.setId(1);
        profile.setAddress("Ravet");
        profile.setPhoneNumber(9028380486L);
        
        ProfileEvent profileEvent = new ProfileEvent();
        profileEvent.setProfileId(1);
        profileEvent.setProfileEventType(ProfileEventType.DELETE);
        profileEvent.setProfile(profile);
		
		when(objectMapper.readValue("Consumer",ProfileEvent.class)).thenReturn(profileEvent);
        
        when(profileRepository.findById(profileEvent.getProfileId())).thenReturn(Optional.of(profile));
        
        ConsumerRecord<Integer,String> consumerRecord = new ConsumerRecord<Integer, String>("profile-event", 0, 0, 0, null, 0, 0, 0, null, "Consumer");
        
        profileEventService.processProfileEvent(consumerRecord);
        
        verify(profileEventService).delete(profileEvent);
      
    }
    
	
	
}
