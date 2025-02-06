package com.charan6700.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/public")
@RestController
public class PublicController {

    @GetMapping("/test")
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Hi Public User!");
    }
}
