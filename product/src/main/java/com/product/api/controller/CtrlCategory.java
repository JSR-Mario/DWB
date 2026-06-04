package com.product.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.api.dto.in.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
@Tag(name = "Category", description = "Administracion de categorias")
public class CtrlCategory {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Listar categorias", description = "Obtiene todas las categorias registradas")
    public ResponseEntity<List<Category>> getCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/active")
    @Operation(summary = "Listar categorias activas", description = "Obtiene solo las categorias con status activo")
    public ResponseEntity<List<Category>> findActiveCategories() {
        return categoryService.findActive();
    }

    @PostMapping
    @Operation(summary = "Registrar categoria", description = "Registra una nueva categoria en el catalogo")
    public ResponseEntity<String> createCategory(@Valid @RequestBody DtoCategoryIn in) {
        return categoryService.create(in);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoria", description = "Actualiza la informacion de una categoria existente")
    public ResponseEntity<String> updateCategory(
            @Valid @RequestBody DtoCategoryIn in,
            @PathVariable Long id) {
        return categoryService.update(in, id);
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "Activar categoria", description = "Activa una categoria deshabilitada")
    public ResponseEntity<String> enableCategory(@PathVariable Long id) {
        return categoryService.enable(id);
    }

    @PatchMapping("/{id}/disable")
    @Operation(summary = "Desactivar categoria", description = "Desactiva una categoria")
    public ResponseEntity<String> disableCategory(@PathVariable Long id) {
        return categoryService.disable(id);
    }
}