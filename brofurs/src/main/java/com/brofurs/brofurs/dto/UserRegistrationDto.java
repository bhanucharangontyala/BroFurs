package com.brofurs.brofurs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

	@NotBlank(message = "Full name is required")
	@Size(min = 2, max = 100, message = "Name must be 2-100 characters")
	private String fullName;

	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email address")
	private String email;

	@NotBlank(message = "Phone is required")
	@Size(max = 20)
	private String phone;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	@NotBlank(message = "Please confirm your password")
	private String confirmPassword;

	@Size(max = 500)
	private String address;

	public boolean passwordsMatch() {
		return password != null && password.equals(confirmPassword);
	}

}