package com.uxpsystems.assignment.entity;

public class ProfileEvent {

    private Integer profileId;
    private ProfileEventType profileEventType;
    
    private UserProfile profile;
    
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
	public UserProfile getProfile() {
		return profile;
	}
	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}
	

}