package com.uxpsystems.assignment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.config.JwtAuthenticationEntryPoint;
import com.uxpsystems.assignment.config.JwtTokenUtil;
import com.uxpsystems.assignment.model.Profile;
import com.uxpsystems.assignment.model.ProfileEvent;
import com.uxpsystems.assignment.producer.ProfileEventProducer;
import com.uxpsystems.assignment.proxy.UserProfileServiceProxy;
import com.uxpsystems.assignment.service.JwtUserDetailsService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ProfileEventControllerUnitTest {

	@InjectMocks
	ProfileController profileController;

	@Spy
	ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	ProfileEventProducer profileEventProducer;

	@Mock
	private JwtTokenUtil jwtTokenUtil;

	@Mock
	private JwtUserDetailsService userDetailsService;

	@Mock
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Mock
	UserProfileServiceProxy proxy;

	@Test
	void updateProfileEventTest() throws Exception {
		// given
		Profile profile = new Profile();
		profile.setId(1);
		profile.setAddress("Hinjewadi");
		profile.setPhoneNumber(9028380486L);

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		when(profileEventProducer.sendProfileEvent(isA(ProfileEvent.class))).thenReturn(null);

		ResponseEntity<?> responseEntity = profileController.updateProfile(profile);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

	}

	@Test
	void updateProfileEventWithNullIdTest() throws Exception {
		// given
		Profile profile = new Profile();
		profile.setId(null);
		profile.setAddress("Hinjewadi");
		profile.setPhoneNumber(9028380486L);

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ResponseEntity<?> responseEntity = profileController.updateProfile(profile);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void deleteProfileEventTest() throws Exception {
		// given
		Integer profileId = 123;

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		when(profileEventProducer.sendProfileEvent(isA(ProfileEvent.class))).thenReturn(null);

		ResponseEntity<?> responseEntity = profileController.deleteProfile(profileId);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isEqualTo("Profile is deleted");

	}

	@Test
	void deleteProfileEventWithNullIdTest() throws Exception {
		// given
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ResponseEntity<?> responseEntity = profileController.deleteProfile(null);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isEqualTo("Please pass the ProfileId");
	}

	@Test
	void createProfileTest() throws Exception {
		// given
		Profile profile = new Profile();
		profile.setAddress("Hinjewadi");
		profile.setPhoneNumber(9028380486L);

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		when(proxy.saveUserProfile(profile)).thenReturn(profile);

		ResponseEntity<?> responseEntity = profileController.createProfile(profile);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

	}

}
