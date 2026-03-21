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

import com.product.api.dto.DtoCategoryIn;
import com.product.api.dto.Response;
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
    public ResponseEntity<Response<List<Category>>> getCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Category>> findActiveCategories() {
        return ResponseEntity.ok(categoryService.findActive());
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody DtoCategoryIn in) {
        return ResponseEntity.ok(categoryService.create(in));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@Valid @RequestBody DtoCategoryIn in, @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.update(in, id));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ApiResponse> enableCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.enable(id));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ApiResponse> disableCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.disable(id));
    }
}