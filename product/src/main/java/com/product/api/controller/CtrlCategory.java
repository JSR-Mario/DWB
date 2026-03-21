package com.product.api.controller;

import com.product.api.entity.Category;
import com.product.api.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.product.api.dto.Response;

import com.product.api.dto.DtoCategoryIn;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CtrlCategory {

    // Inyección de dependencias por constructor
    /*
     * private final CategoryService categoryService;
     * 
     * public CtrlCategory(CategoryService categoryService) {
     * this.categoryService = categoryService;
     * }
     */

    // Inyección de dependencias por campo
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Category>> findActiveCategories() {
        return ResponseEntity.ok(categoryService.findActive());
    }

    @PostMapping
    public ResponseEntity<Response> createCategory(@Valid @RequestBody DtoCategoryIn in) {
        categoryService.create(in);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("La categoria ha sido registrada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateCategory(
            @Valid @RequestBody DtoCategoryIn in,
            @PathVariable Long id) {
        categoryService.update(in, id);
        return ResponseEntity.ok(new Response("La categoria ha sido actualizada"));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<Response> enableCategory(@PathVariable Long id) {
        categoryService.enable(id);
        return ResponseEntity.ok(new Response("La categoria ha sido habilitada"));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Response> disableCategory(@PathVariable Long id) {
        categoryService.disable(id);
        return ResponseEntity.ok(new Response("La categoria ha sido deshabilitada"));
    }
}