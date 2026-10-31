package org.com.controller;

import org.com.dto.AuthRequest;
import org.com.dto.AuthResponse;
import org.com.service.Iservices.IUserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IUserServices userServices;

    public AuthController(IUserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = this.userServices.login(authRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

}
