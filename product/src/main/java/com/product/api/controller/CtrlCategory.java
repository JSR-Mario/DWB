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

@RestController
@RequestMapping("/category")
@Tag(name = "Category", description = "Catálogo de categorías")
public class CtrlCategory {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/active")
    public ResponseEntity<List<Category>> findActiveCategories() {
        return categoryService.findActive();
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@Valid @RequestBody DtoCategoryIn in) {
        return categoryService.create(in);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
            @Valid @RequestBody DtoCategoryIn in,
            @PathVariable Long id) {
        return categoryService.update(in, id);
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<String> enableCategory(@PathVariable Long id) {
        return categoryService.enable(id);
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> disableCategory(@PathVariable Long id) {
        return categoryService.disable(id);
    }
}