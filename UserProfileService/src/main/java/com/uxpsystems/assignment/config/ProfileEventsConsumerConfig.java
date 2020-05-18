package com.uxpsystems.assignment.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.uxpsystems.assignment.service.ProfileEventsService;

@Configuration
@EnableKafka
public class ProfileEventsConsumerConfig {

	Logger logger = LoggerFactory.getLogger(ProfileEventsConsumerConfig.class);

	@Autowired
	ProfileEventsService profileEventsService;

	@Bean
	ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			ConsumerFactory<Object, Object> kafkaConsumerFactory) {
		
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		configurer.configure(factory, kafkaConsumerFactory);
		factory.setConcurrency(3);

		factory.setErrorHandler(((thrownException, data) -> {
			logger.info("Exception in consumerConfig is {} and the record is {}", thrownException.getMessage(), data);

		}));
		factory.setRetryTemplate(retryTemplate());
		factory.setRecoveryCallback((context -> {
			if (context.getLastThrowable().getCause() instanceof RecoverableDataAccessException) {
				// invoke recovery logic
				logger.info("Inside the recoverable logic");
				/*
				 * Arrays.asList(context.attributeNames()) .forEach(attributeName -> {
				 * log.info("Attribute name is : {} ", attributeName);
				 * log.info("Attribute Value is : {} ", context.getAttribute(attributeName));
				 * });
				 */

				ConsumerRecord<Integer, String> consumerRecord = (ConsumerRecord<Integer, String>) context
						.getAttribute("record");
				profileEventsService.handleRecovery(consumerRecord);
			} else {
				logger.info("Inside the non recoverable logic");
				throw new RuntimeException(context.getLastThrowable().getMessage());
			}

			return null;
		}));
		return factory;
	}

	private RetryTemplate retryTemplate() {

		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(1000);
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(simpleRetryPolicy());
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
		return retryTemplate;
	}

	private RetryPolicy simpleRetryPolicy() {

		Map<Class<? extends Throwable>, Boolean> exceptionsMap = new HashMap<>();
		exceptionsMap.put(IllegalArgumentException.class, false);
		exceptionsMap.put(RecoverableDataAccessException.class, true);
		SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, exceptionsMap, true);
		return simpleRetryPolicy;
	}
}