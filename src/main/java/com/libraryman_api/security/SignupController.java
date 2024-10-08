package com.libraryman_api.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.libraryman_api.member.Members;


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
}
