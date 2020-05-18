package com.uxpsystems.assignment.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ProfileEvent {

	private Integer profileId;
	private ProfileEventType profileEventType;
	@NotNull
	@Valid
	private Profile profile;

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public ProfileEventType getProfileEventType() {
		return profileEventType;
	}

	public void setProfileEventType(ProfileEventType profileEventType) {
		this.profileEventType = profileEventType;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

}