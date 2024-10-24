package com.libraryman_api.security.services;

import com.libraryman_api.member.MemberRepository;
import com.libraryman_api.security.jwt.JwtAuthenticationHelper;
import com.libraryman_api.security.model.LoginRequest;
import com.libraryman_api.security.model.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationHelper jwtHelper;

    private final MemberRepository memberRepository;

    public LoginService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtAuthenticationHelper jwtHelper, MemberRepository memberRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
        this.memberRepository = memberRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtHelper.generateToken(userDetails);
        LoginResponse loginResponse = new LoginResponse(token);
        return loginResponse;
    }

    public void Authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticateToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authenticateToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}
