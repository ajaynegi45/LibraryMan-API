package com.libraryman_api.newsletter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/newsletter")
public class NewsletterController {

    private final NewsletterService newsletterService;

    @Autowired
    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam String email) {
        return newsletterService.subscribe(email);
    }

    @GetMapping("/unsubscribe")
    public String unsubscribe(@RequestParam String token) {
        return newsletterService.unsubscribe(token);
    }
}
