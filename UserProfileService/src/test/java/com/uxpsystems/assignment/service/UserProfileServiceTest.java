package com.uxpsystems.assignment.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uxpsystems.assignment.dao.ProfileRepository;
import com.uxpsystems.assignment.entity.UserProfile;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {
	
	@InjectMocks
	UserProfileService userProfileService;
	
	@Mock
	ProfileRepository profileRepository;
	
	@Test
    public void saveProfileTest()
    {
		UserProfile profile = new UserProfile();
        profile.setId(1);
        profile.setAddress("Ravet");
        profile.setPhoneNumber(9028380486L);
        
        when(profileRepository.save(profile)).thenReturn(profile);
        
        UserProfile savedProfile=userProfileService.saveProfile(profile);
        assertEquals("Ravet", savedProfile.getAddress());
      
    }
	
}
