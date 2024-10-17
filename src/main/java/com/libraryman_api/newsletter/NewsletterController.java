package com.libraryman_api.newsletter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/newsletter")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    // Subscribe endpoint
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");

        // Call the service to handle subscription
        String response = newsletterService.subscribe(email);

        // Return response from the service
        if (response.equals("Invalid email format.") || response.equals("Email is already subscribed.")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    // Unsubscribe endpoint using token
    @GetMapping("/unsubscribe/{token}")
    public ResponseEntity<String> unsubscribe(@PathVariable String token) {
        String response = newsletterService.unsubscribe(token);

        // Check if the response indicates an error
        if (response.equals("Invalid or expired token.") || response.equals("You are already unsubscribed.")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
