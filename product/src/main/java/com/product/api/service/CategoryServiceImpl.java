package com.product.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.repository.CategoryRepository;
import com.product.exception.ApiException;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repoCategory;

    @Override
    public List<Category> findAll() {
        List<Category> categories = repoCategory.findAllByOrderByCategoryAsc();
        if (categories.isEmpty()) {
            throw new ApiException("No se encontraron categorias", HttpStatus.NOT_FOUND);
        }
        return categories;
    }

    @Override
    public List<Category> findActive() {
        List<Category> categories = repoCategory.findByStatusOrderByCategoryAsc(1);
        if (categories.isEmpty()) {
            throw new ApiException("No se encontraron categorias activas", HttpStatus.NOT_FOUND);
        }
        return categories;
    }

    @Override
    public void create(DtoCategoryIn in) {
        boolean exists = repoCategory.existsByCategory(in.getCategory());
        if (exists) {
            throw new ApiException("La categoria ya existe: " + in.getCategory(), HttpStatus.CONFLICT);
        }

        Category category = new Category();
        category.setCategory(in.getCategory());
        category.setTag(in.getTag());
        category.setStatus(1);

        repoCategory.save(category);
    }

    @Override
    public void update(DtoCategoryIn in, Long categoryID) {
        Category category = repoCategory.findById(categoryID)
                .orElseThrow(
                        () -> new ApiException("La categoria no existe con id: " + categoryID, HttpStatus.NOT_FOUND));

        boolean existsWithSameName = repoCategory.existsByCategoryAndCategoryIDNot(in.getCategory(), categoryID);
        if (existsWithSameName) {
            throw new ApiException("El nombre de la categoria ya esta en uso: " + in.getCategory(),
                    HttpStatus.CONFLICT);
        }

        category.setCategory(in.getCategory());
        category.setTag(in.getTag());
        repoCategory.save(category);
    }

    @Override
    public void enable(Long categoryID) {
        Category category = repoCategory.findById(categoryID)
                .orElseThrow(
                        () -> new ApiException("La categoria no existe con id: " + categoryID, HttpStatus.NOT_FOUND));

        // no se si aqui sea tan buena idea validar pero lo dejo
        if (category.getStatus() == 1) {
            throw new ApiException("La categoria ya esta habilitada", HttpStatus.BAD_REQUEST);
        }

        category.setStatus(1);
        repoCategory.save(category);
    }

    @Override
    public void disable(Long categoryID) {
        Category category = repoCategory.findById(categoryID)
                .orElseThrow(
                        () -> new ApiException("La categoria no existe con id: " + categoryID, HttpStatus.NOT_FOUND));

        if (category.getStatus() == 0) {
            throw new ApiException("La categoria ya esta deshabilitada", HttpStatus.BAD_REQUEST);
        }

        category.setStatus(0);
        repoCategory.save(category);
    }
}