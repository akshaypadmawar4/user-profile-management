package com.uxpsystems.assignment.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.model.Profile;
import com.uxpsystems.assignment.model.ProfileEvent;
import com.uxpsystems.assignment.model.ProfileEventType;

@ExtendWith(MockitoExtension.class)
public class ProfileEventProducerUnitTest {

	@Mock
	KafkaTemplate<Integer, String> kafkaTemplate;

	@Spy
	ObjectMapper objectMapper = new ObjectMapper();

	@InjectMocks
	ProfileEventProducer eventProducer;

	@Test
	void sendProfileEventFailureTest() throws JsonProcessingException, ExecutionException, InterruptedException {
		// given

		Profile profile = new Profile();
		profile.setId(1);
		profile.setAddress("Hinjewadi");
		profile.setPhoneNumber(9028380486L);

		ProfileEvent profileEvent = new ProfileEvent();
		profileEvent.setProfileId(null);
		profileEvent.setProfileEventType(ProfileEventType.UPDATE);
		profileEvent.setProfile(profile);

		SettableListenableFuture future = new SettableListenableFuture();

		future.setException(new RuntimeException("Exception Calling Kafka"));
		when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
		// when

		assertThrows(Exception.class, () -> eventProducer.sendProfileEvent(profileEvent).get());

	}

	@Test
	void sendProfileEvenSuccessTest() throws JsonProcessingException, ExecutionException, InterruptedException {
		// given
		Profile profile = new Profile();
		profile.setId(1);
		profile.setAddress("Hinjewadi");
		profile.setPhoneNumber(9028380486L);

		ProfileEvent profileEvent = new ProfileEvent();
		profileEvent.setProfileId(1);
		profileEvent.setProfileEventType(ProfileEventType.UPDATE);
		profileEvent.setProfile(profile);

		String record = objectMapper.writeValueAsString(profileEvent);
		SettableListenableFuture<SendResult<Integer, String>> future = new SettableListenableFuture<SendResult<Integer, String>>();

		ProducerRecord<Integer, String> producerRecord = new ProducerRecord<Integer, String>("profile-events",
				profileEvent.getProfileId(), record);
		RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("profile-events", 1), 1, 1, 342,
				System.currentTimeMillis(), 1, 2);
		SendResult<Integer, String> sendResult = new SendResult<Integer, String>(producerRecord, recordMetadata);

		future.set(sendResult);
		when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
		// when

		ListenableFuture<SendResult<Integer, String>> listenableFuture = eventProducer.sendProfileEvent(profileEvent);

		// then
		SendResult<Integer, String> sendResult1 = listenableFuture.get();
		assertEquals(sendResult1.getRecordMetadata().partition() , 1);

	}
}
