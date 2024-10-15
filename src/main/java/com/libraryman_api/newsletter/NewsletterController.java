package com.libraryman_api.newsletter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/newsletter")
public class NewsletterController {
    @Autowired
    private NewsletterService newsletterService;

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");

        // Validate the email field
        if (email == null || !newsletterService.isValidEmail(email)) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        // Process the subscription
        String response = newsletterService.subscribe(email);
        return ResponseEntity.ok(response);
    }
}
