package com.uxpsystems.assignment.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.uxpsystems.assignment.model.Profile;

@FeignClient(name = "user-profile-service", url = "localhost:9091")
public interface UserProfileServiceProxy {

	@PostMapping("/profile")
	public Profile saveUserProfile(@RequestBody Profile userProfile);

}
