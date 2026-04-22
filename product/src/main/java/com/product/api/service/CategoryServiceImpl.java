package com.product.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.product.api.dto.in.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.repository.CategoryRepository;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repoCategory;

    @Override
    public ResponseEntity<List<Category>> findAll() {
        try {
            List<Category> categories = repoCategory.findAllByOrderByCategoryAsc();
            if (categories.isEmpty()) {
                throw new ApiException("No se encontraron categorias", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }

    @Override
    public ResponseEntity<List<Category>> findActive() {
        try {
            List<Category> categories = repoCategory.findByStatusOrderByCategoryAsc(1);
            if (categories.isEmpty()) {
                throw new ApiException("No se encontraron categorias activas", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }

    @Override
    public ResponseEntity<String> create(DtoCategoryIn in) {
        try {
            if (repoCategory.existsByCategory(in.getCategory())) {
                throw new ApiException("La categoria ya existe: " + in.getCategory(), HttpStatus.CONFLICT);
            }

            Category category = new Category();
            category.setCategory(in.getCategory());
            category.setTag(in.getTag());
            category.setStatus(1);

            repoCategory.save(category);
            return new ResponseEntity<>("La categoria ha sido registrada", HttpStatus.CREATED);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }

    @Override
    public ResponseEntity<String> update(DtoCategoryIn in, Long categoryID) {
        try {
            Category category = repoCategory.findById(categoryID)
                    .orElseThrow(() -> new ApiException(
                            "La categoria no existe con id: " + categoryID, HttpStatus.NOT_FOUND));

            if (repoCategory.existsByCategoryAndCategoryIDNot(in.getCategory(), categoryID)) {
                throw new ApiException(
                        "El nombre de la categoria ya esta en uso: " + in.getCategory(), HttpStatus.CONFLICT);
            }

            category.setCategory(in.getCategory());
            category.setTag(in.getTag());
            repoCategory.save(category);
            return new ResponseEntity<>("La categoria ha sido actualizada", HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }

    @Override
    public ResponseEntity<String> enable(Long categoryID) {
        try {
            Category category = repoCategory.findById(categoryID)
                    .orElseThrow(() -> new ApiException(
                            "La categoria no existe con id: " + categoryID, HttpStatus.NOT_FOUND));

            // no se si aqui sea tan buena idea validar pero lo dejo
            if (category.getStatus() == 1) {
                throw new ApiException("La categoria ya esta habilitada", HttpStatus.BAD_REQUEST);
            }

            category.setStatus(1);
            repoCategory.save(category);
            return new ResponseEntity<>("La categoria ha sido habilitada", HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }

    @Override
    public ResponseEntity<String> disable(Long categoryID) {
        try {
            Category category = repoCategory.findById(categoryID)
                    .orElseThrow(() -> new ApiException(
                            "La categoria no existe con id: " + categoryID, HttpStatus.NOT_FOUND));

            if (category.getStatus() == 0) {
                throw new ApiException("La categoria ya esta deshabilitada", HttpStatus.BAD_REQUEST);
            }

            category.setStatus(0);
            repoCategory.save(category);
            return new ResponseEntity<>("La categoria ha sido deshabilitada", HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }
}