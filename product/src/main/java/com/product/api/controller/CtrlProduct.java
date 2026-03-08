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
public class CtrlProduct {

    @Autowired
    CategoryService svc;

    @GetMapping
    public List<Category> getCategories() {
        return svc.getCategories();
    }
}
