package com.libraryman_api.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.libraryman_api.member.MemberRepository;


@Service
public class LoginService {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtAuthenticationHelper jwtHelper;
	
	@Autowired
	MemberRepository memberRepository;
	
	public LoginResponse login(LoginRequest loginRequest) {
		Authenticate(loginRequest.getUsername(), loginRequest.getPassword());
		UserDetails userDetails=userDetailsService.loadUserByUsername(loginRequest.getUsername());
		String token=jwtHelper.generateToken(userDetails);
		LoginResponse loginResponse=new LoginResponse(token);
		return loginResponse;	
	}
	
	public void Authenticate(String username,String password) {
		UsernamePasswordAuthenticationToken authenticateToken=new UsernamePasswordAuthenticationToken(username, password);
		try {
			authenticationManager.authenticate(authenticateToken);
		}
		catch(BadCredentialsException e){
			throw new BadCredentialsException("Invalid Username or Password");
		}
	}
}
