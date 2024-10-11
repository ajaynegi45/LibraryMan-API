package com.libraryman_api.security.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.libraryman_api.member.Members;
import com.libraryman_api.security.services.SignupService;


@RestController
public class SignupController {
	
	private SignupService signupService;
	public SignupController(SignupService signupService) {
		this.signupService=signupService;
	}
	
	@PostMapping("/api/signup")
	public void signup(@RequestBody Members members) {
		this.signupService.signup(members);
		
	}
	@PostMapping("/api/signup/admin")
	public void signupAdmin(@RequestBody Members members) {
		this.signupService.signupAdmin(members);
	}
	@PostMapping("/api/signup/librarian")
	public void signupLibrarian(@RequestBody Members members) {
		this.signupService.signupLibrarian(members);
	}
}
