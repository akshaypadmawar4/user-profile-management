package com.uxpsystems.assignment.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Profile {

	private Integer id;
	@NotBlank
	private String address;
	@NotNull
	private Long phoneNumber;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "UserProfile [id=" + id + ", address=" + address + ", phoneNumber=" + phoneNumber + "]";
	}

}
