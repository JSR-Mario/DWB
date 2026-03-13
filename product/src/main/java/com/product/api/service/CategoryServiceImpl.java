package com.product.api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.product.api.entity.Category;
import com.product.api.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    // Inyección de dependencias por constructor
    /*
     * private final CategoryRepository repoCategory;
     * 
     * public CategoryServiceImpl(CategoryRepository repoCategory) {
     * this.repoCategory = repoCategory;
     * }
     */

    // Inyección de dependencias por campo
    @Autowired
    private CategoryRepository repoCategory;

    @Override
    public List<Category> getCategories() {
        return repoCategory.findAllByOrderByCategoryAsc();
    }
}
