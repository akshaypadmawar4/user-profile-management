package com.uxpsystems.assignment.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uxpsystems.assignment.service.ProfileEventsService;

@Component
public class ProfileEventsConsumer {
	
	Logger logger = LoggerFactory.getLogger(ProfileEventsConsumer.class);

    @Autowired
    private ProfileEventsService profileEventsService;

    @KafkaListener(topics = {"profile-events"})
    public void onMessage(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {

        logger.info("ConsumerRecord : {} ", consumerRecord );
        profileEventsService.processProfileEvent(consumerRecord);

    }
}
