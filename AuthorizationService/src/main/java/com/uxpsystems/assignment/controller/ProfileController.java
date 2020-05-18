package com.uxpsystems.assignment.controller;

import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uxpsystems.assignment.model.Profile;
import com.uxpsystems.assignment.model.ProfileEvent;
import com.uxpsystems.assignment.model.ProfileEventType;
import com.uxpsystems.assignment.producer.ProfileEventProducer;
import com.uxpsystems.assignment.proxy.UserProfileServiceProxy;

@RestController
public class ProfileController {

	@Autowired
	UserProfileServiceProxy proxy;

	@Autowired
	ProfileEventProducer profileEventProducer;

	@PostMapping(value = "/profile")
	public ResponseEntity<Profile> createProfile(@RequestBody @Valid Profile userProfile) throws Exception {

		Profile response = proxy.saveUserProfile(userProfile);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/profile")
	public ResponseEntity<?> updateProfile(@RequestBody @Valid Profile userProfile)
			throws JsonProcessingException, ExecutionException, InterruptedException {

		if (userProfile.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the ProfileId");
		}

		ProfileEvent profileEvent = new ProfileEvent();
		profileEvent.setProfileEventType(ProfileEventType.UPDATE);
		profileEvent.setProfileId(userProfile.getId());
		profileEvent.setProfile(userProfile);

		profileEventProducer.sendProfileEvent(profileEvent);
		return ResponseEntity.status(HttpStatus.OK).body(profileEvent.getProfile());
	}

	@DeleteMapping("/profile/{profileId}")
	public ResponseEntity<?> deleteProfile(@PathVariable Integer profileId)
			throws JsonProcessingException, ExecutionException, InterruptedException {

		if (profileId == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the ProfileId");
		}

		ProfileEvent profileEvent = new ProfileEvent();
		profileEvent.setProfileEventType(ProfileEventType.DELETE);
		profileEvent.setProfileId(profileId);

		profileEventProducer.sendProfileEvent(profileEvent);
		return ResponseEntity.status(HttpStatus.OK).body("Profile is deleted");
	}

}