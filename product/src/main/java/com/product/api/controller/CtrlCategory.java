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

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/category")
@Tag(name = "Category", description = "Catálogo de categorías")
public class CtrlCategory {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista con todas las categorías registradas, sin importar su estatus. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token faltante o inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tienes rol ADMIN", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        return categoryService.findAll();
    }

    @Operation(summary = "Obtener categorías activas", description = "Devuelve una lista únicamente con las categorías activas. Accesible por ADMIN y CUSTOMER.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    @GetMapping("/active")
    public ResponseEntity<List<Category>> findActiveCategories() {
        return categoryService.findActive();
    }

    @Operation(summary = "Crear categoría", description = "Registra una nueva categoría en el sistema. Válida que el nombre o tag no estén duplicados. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría creada exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Petición inválida (datos faltantes o duplicados)", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido - Requiere rol ADMIN", content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> createCategory(@Valid @RequestBody DtoCategoryIn in) {
        return categoryService.create(in);
    }

    @Operation(summary = "Actualizar categoría", description = "Modifica los datos de una categoría existente mediante su ID. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o campos duplicados con otra categoría", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
            @Valid @RequestBody DtoCategoryIn in,
            @PathVariable Long id) {
        return categoryService.update(in, id);
    }

    @Operation(summary = "Activar categoría", description = "Cambia el estatus de la categoría a Activo. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría activada exitosamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @PatchMapping("/{id}/enable")
    public ResponseEntity<String> enableCategory(@PathVariable Long id) {
        return categoryService.enable(id);
    }

    @Operation(summary = "Desactivar categoría", description = "Cambia el estatus de la categoría a Inactivo. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría desactivada exitosamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> disableCategory(@PathVariable Long id) {
        return categoryService.disable(id);
    }
}