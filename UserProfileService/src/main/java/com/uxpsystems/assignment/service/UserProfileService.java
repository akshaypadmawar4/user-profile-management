package com.uxpsystems.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxpsystems.assignment.dao.ProfileRepository;
import com.uxpsystems.assignment.entity.UserProfile;

@Service
public class UserProfileService {

	@Autowired
	ProfileRepository profileRepository;
	
	public UserProfile saveProfile(UserProfile userProfile) {
		return profileRepository.save(userProfile);
	}
	
}
