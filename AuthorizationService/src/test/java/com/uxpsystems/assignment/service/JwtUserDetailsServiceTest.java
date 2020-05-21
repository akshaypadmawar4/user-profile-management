package com.uxpsystems.assignment.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.uxpsystems.assignment.dao.DAOUser;
import com.uxpsystems.assignment.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class JwtUserDetailsServiceTest {

	@InjectMocks
	JwtUserDetailsService jwtUserDetailsService;

	@Mock
	UserRepository userRepository;

	@Test
	public void loadUserByUsernameTest() {
		String username = "user";
		DAOUser daoUser = new DAOUser();
		daoUser.setUsername("user");
		daoUser.setPassword("pass");

		when(userRepository.findByUsername(username)).thenReturn(daoUser);
		String pass = jwtUserDetailsService.loadUserByUsername(username).getPassword();
		assertEquals("pass", pass);
	}

	@Test
	public void loadUserByUsernameExceptionTest() {
		String username = "user";

		when(userRepository.findByUsername(username)).thenReturn(null);

		assertThrows(UsernameNotFoundException.class, () -> jwtUserDetailsService.loadUserByUsername(username));
	}

}
