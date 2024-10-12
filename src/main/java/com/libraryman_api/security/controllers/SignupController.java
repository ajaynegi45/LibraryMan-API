package com.libraryman_api.security.controllers;

import org.springframework.web.bind.annotation.PathVariable;
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
	@PostMapping("/api/signup/admin/{secretKey}")
	public void signupAdmin(@RequestBody Members members,@PathVariable String secretKey) {
		this.signupService.signupAdmin(members,secretKey);
	}
	@PostMapping("/api/signup/librarian/{secretKey}")
	public void signupLibrarian(@RequestBody Members members,@PathVariable String secretKey) {
		this.signupService.signupLibrarian(members,secretKey);
	}
}
