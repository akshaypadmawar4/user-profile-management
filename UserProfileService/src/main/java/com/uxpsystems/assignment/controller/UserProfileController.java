package com.uxpsystems.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uxpsystems.assignment.entity.UserProfile;
import com.uxpsystems.assignment.service.UserProfileService;

@RestController
public class UserProfileController {
	
	@Autowired
	UserProfileService userProfileService;
	
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public ResponseEntity<UserProfile> profileCreation(@RequestBody UserProfile userProfile) throws Exception {

		UserProfile userProfileCreated= userProfileService.saveProfile(userProfile);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userProfileCreated);
	}
	

}