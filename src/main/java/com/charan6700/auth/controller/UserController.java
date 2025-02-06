package com.charan6700.auth.controller;

import com.charan6700.auth.dto.GraphUserDataResponse;
import com.charan6700.auth.dto.User;
import com.charan6700.auth.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private GraphService graphService;

    @GetMapping("/user")
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Hi User!");
    }

    @GetMapping("/test")
    public String secureEndpoint(Principal principal) {
        return "Hello, " + principal.getName() + "! You have accessed a protected resource.";
    }

    @GetMapping("/users")
    public List<User> getUsers(@RequestHeader("Authorization") String token) {
        GraphUserDataResponse response = graphService.getAllUsers(token);
        return response.getValue();
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestParam String recipient) {
        String response = graphService.sendEmail(recipient, "Test Email", "This is a test email sent via Microsoft Graph API.");
        return ResponseEntity.ok(response);
    }
}
