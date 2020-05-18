package com.uxpsystems.assignment.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.uxpsystems.assignment.dao.ProfileRepository;
import com.uxpsystems.assignment.entity.UserProfile;

@SpringBootTest
public class UserProfileServiceTest {
	
	@Autowired
	UserProfileService userProfileService;
	
	@MockBean
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
