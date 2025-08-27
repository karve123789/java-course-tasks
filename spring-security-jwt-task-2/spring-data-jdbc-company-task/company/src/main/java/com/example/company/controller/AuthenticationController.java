package com.example.company.controller;
import com.example.company.dto.JwtAuthenticationResponse;
import com.example.company.dto.SignInRequest;
import com.example.company.dto.SignUpRequest;
import com.example.company.model.User;
import com.example.company.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }


    @PostMapping("/unlock/{username}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> unlockUser(@PathVariable String username) {
        authenticationService.unlockUser(username);
        return ResponseEntity.ok("Пользователь " + username + " успешно разблокирован.");
    }
}