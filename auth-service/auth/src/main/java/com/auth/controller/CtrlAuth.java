package com.auth.controller;

import com.auth.dto.in.LoginRequest;
import com.auth.entity.User;
import com.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@Tag(name = "Auth", description = "Autenticacion y generacion de tokens JWT")
public class CtrlAuth {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Autentica al usuario y devuelve un token JWT valido por 1 hora")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        User user   = (User) auth.getPrincipal();
        String token = jwtUtil.generateToken(user);

        return Map.of("token", token);
    }
}
