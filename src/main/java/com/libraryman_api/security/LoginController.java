package com.libraryman_api.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LoginController {

    @GetMapping("/api/ajay")
    public String login(Principal principal) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Principal Name: " + principal.getName());
        System.out.println("Principal: " + principal);

        return "Hello World";
    }

}
