package com.uxpsystems.assignment.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.entity.UserProfile;
import com.uxpsystems.assignment.service.UserProfileService;


@WebMvcTest(UserProfileController.class)
@AutoConfigureMockMvc
public class ProfileEventControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    UserProfileService userProfileService;
  

    @Test
    void profileCreationTest() throws Exception {

        //given
    	UserProfile profile = new UserProfile();
        profile.setId(1);
        profile.setAddress("Ravet");
        profile.setPhoneNumber(9028380486L);
        
        String json = objectMapper.writeValueAsString(profile);
        when(userProfileService.saveProfile(isA(UserProfile.class))).thenReturn(profile);

        //expect
        mockMvc.perform(
                post("/profile")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
     }
}
