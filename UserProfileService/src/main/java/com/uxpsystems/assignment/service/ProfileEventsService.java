package com.uxpsystems.assignment.service;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.dao.ProfileRepository;
import com.uxpsystems.assignment.entity.ProfileEvent;
import com.uxpsystems.assignment.entity.UserProfile;

@Service
public class ProfileEventsService {

	Logger logger = LoggerFactory.getLogger(ProfileEventsService.class);
	
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<Integer,String> kafkaTemplate;

    @Autowired
    private ProfileRepository profileRepository;

    public void processProfileEvent(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        ProfileEvent profileEvent = objectMapper.readValue(consumerRecord.value(), ProfileEvent.class);
        logger.info("profileEvent : {} ", profileEvent);

        switch(profileEvent.getProfileEventType()){
            case UPDATE:
                //validate the profileevent
                validate(profileEvent);
                update(profileEvent);
                break;
            case DELETE:
            	validate(profileEvent);
            	delete(profileEvent);
                break;
            default:
                logger.info("Invalid Library Event Type");
        }

    }

    private void validate(ProfileEvent profileEvent) {
        if(profileEvent.getProfileId()==null){
            throw new IllegalArgumentException("Profile Event Id is missing");
        }

        Optional<UserProfile> userProfileOptional = profileRepository.findById(profileEvent.getProfileId());
        if(!userProfileOptional.isPresent()){
            throw new IllegalArgumentException("Not a valid profile Event");
        }
        logger.info("Validation is successful for the profile Event : {} ", userProfileOptional.get());
    }

    private void update(ProfileEvent profileEvent) {
    	Optional<UserProfile> userProfileOptional = profileRepository.findById(profileEvent.getProfileId());
    	UserProfile userProfile=userProfileOptional.get();
    	userProfile.setAddress(profileEvent.getProfile().getAddress());
    	userProfile.setPhoneNumber(profileEvent.getProfile().getPhoneNumber());
    	profileRepository.save(userProfile);
        logger.info("Successfully Persisted the profile Event {} ", userProfile);
    }
    
    private void delete(ProfileEvent profileEvent) {
    	Optional<UserProfile> userProfileOptional = profileRepository.findById(profileEvent.getProfileId());
    	UserProfile userProfile=userProfileOptional.get();
    	profileRepository.delete(userProfile);
        logger.info("Successfully deleted the profile Event {} ", userProfile);
    }

    public void handleRecovery(ConsumerRecord<Integer,String> record){

        Integer key = record.key();
        String message = record.value();

        ListenableFuture<SendResult<Integer,String>> listenableFuture = kafkaTemplate.sendDefault(key, message);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, message, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, message, result);
            }
        });
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        logger.error("Error Sending the Message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            logger.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        logger.info("Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }
}
