package com.product.api.controller;

import com.product.api.entity.Category;
import com.product.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }
}