package com.libraryman_api.security;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private JwtAuthenticationHelper jwtHelper;
	
	private UserDetailsService userDetailsService;
	
	public JwtAuthenticationFilter(JwtAuthenticationHelper jwtHelper,UserDetailsService userDetailsService) {
		this.jwtHelper=jwtHelper;
		this.userDetailsService=userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String requestHeader=request.getHeader("Authorization");
		String username=null;
		String token=null;
		if(requestHeader!=null && requestHeader.startsWith("Bearer ")) {
			token=requestHeader.substring(7);
			username=jwtHelper.getUsernameFromToken(token);
			if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails=userDetailsService.loadUserByUsername(username);
				if(!(jwtHelper.isTokenExpired(token))) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					
				}
				else {
	                System.out.println("Token is expired or user details not found.");
	            }
			}
			
		}
		filterChain.doFilter(request, response);
		}
		
	

	
}
