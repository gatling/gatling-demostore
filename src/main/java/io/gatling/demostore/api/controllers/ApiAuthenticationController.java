package io.gatling.demostore.api.controllers;

import io.gatling.demostore.api.payloads.AuthenticationRequest;
import io.gatling.demostore.api.payloads.AuthenticationResponse;
import io.gatling.demostore.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/authenticate")
public class ApiAuthenticationController {

    public ApiAuthenticationController(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping
    public AuthenticationResponse createAuthenticationToken(
            @Valid @RequestBody AuthenticationRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        if (userDetails == null || !userDetails.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String token = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(token);
    }
}
