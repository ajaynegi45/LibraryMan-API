package com.libraryman_api.security.controllers;

import com.libraryman_api.security.model.LoginRequest;
import com.libraryman_api.security.model.LoginResponse;
import com.libraryman_api.security.services.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = loginService.login(loginRequest);

        if (loginResponse != null) {
            setAuthCookie(response);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private void setAuthCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("LibraryManCookie", "libraryman_cookie");
        cookie.setMaxAge(3600); // (3600 seconds)
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
