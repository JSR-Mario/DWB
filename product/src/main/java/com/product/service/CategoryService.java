package com.product.service;

import com.product.model.Category;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service 
public class CategoryService{
    // Agrego unas categorias de ejemplo
    private final List<Category> categories = Arrays.asList(
        new Category(1, "Lentes","Lts",1),
        new Category(2, "Relojes","Rljs",2)
    );

    public List<Category> getAll(){ return categories; }
}

