package com.auth.controller;

import com.auth.dto.in.UserRequest;
import com.auth.dto.out.UserResponse;
import com.auth.service.SvcUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Administracion de usuarios")
public class CtrlUser {

    @Autowired
    private SvcUser svcUser;

    @PostMapping
    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario con rol CUSTOMER por defecto")
    public String create(@Valid @RequestBody UserRequest request) {
        return svcUser.createUser(request);
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados (solo Administrator)")
    public List<UserResponse> getUsers() {
        return svcUser.getUsers();
    }

}