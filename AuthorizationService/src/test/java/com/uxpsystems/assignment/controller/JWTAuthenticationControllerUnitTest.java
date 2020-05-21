package com.uxpsystems.assignment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.uxpsystems.assignment.config.JwtAuthenticationEntryPoint;
import com.uxpsystems.assignment.config.JwtTokenUtil;
import com.uxpsystems.assignment.model.JwtRequest;
import com.uxpsystems.assignment.model.JwtResponse;
import com.uxpsystems.assignment.producer.ProfileEventProducer;
import com.uxpsystems.assignment.service.JwtUserDetailsService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class JWTAuthenticationControllerUnitTest {

	@InjectMocks
	JwtAuthenticationController jwtAuthnticationController;

	@Mock
	ProfileEventProducer profileEventProducer;

	@Mock
	private JwtTokenUtil jwtTokenUtil;

	@Mock
	private JwtUserDetailsService userDetailsService;

	@Mock
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Mock
	AuthenticationManager authnticationManager;

	@Test
	void loginTest() throws Exception {
		// given
		JwtRequest jwtRequest = new JwtRequest();
		jwtRequest.setUsername("user");
		jwtRequest.setPassword("pass");

		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBa3NoYXkiLCJleHAiOjE1ODk3Mzk5NDQsImlhdCI6MTU4OTcyMTk0NH0.NgiIyx1-qsexNzX-NqWMkliWTyL5FyIshnf5rtYuVWl0zHxJuLIdjxMMjY8kh_9Na6AoXWdgENkojja1zmP2VA";

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(jwtRequest.getUsername(),
				jwtRequest.getPassword(), new ArrayList<>());

		when(authnticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

		when(userDetailsService.loadUserByUsername(jwtRequest.getUsername())).thenReturn(userDetails);

		when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);

		ResponseEntity<?> responseEntity = jwtAuthnticationController.createAuthenticationToken(jwtRequest);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(((JwtResponse) responseEntity.getBody()).getToken()).isEqualTo(token);
	}

	@Test
	void loginTestWithBadCredential() throws Exception {
		// given
		JwtRequest jwtRequest = new JwtRequest();
		jwtRequest.setUsername("user123");
		jwtRequest.setPassword("pass");

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		when(authnticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(BadCredentialsException.class);

		assertThrows(Exception.class, () -> jwtAuthnticationController.createAuthenticationToken(jwtRequest));
	}

}
