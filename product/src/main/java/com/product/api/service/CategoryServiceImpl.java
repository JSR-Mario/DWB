package com.product.api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.product.api.entity.Category;
import com.product.api.dto.DtoCategoryIn;
import com.product.api.repository.CategoryRepository;
import com.product.api.dto.Response;
import com.product.exception.ApiException;
import org.springframework.dao.DataAccessException;

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
    public Response<List<Category>> findAll() {
        try {
            return new Response<>(repoCategory.findAllByOrderByCategoryAsc());
        } catch (DataAccessException e) {
            throw new ApiException("Error en la consulta de categorias");
        }
    }

    @Override
    public List<Category> findActive() {
        return null; // TODO: Implement
    }

    @Override
    public void create(DtoCategoryIn in) {
        // TODO: Implement
    }

    @Override
    public void update(DtoCategoryIn in, Long categoryID) {
        // TODO: Implement
    }

    @Override
    public void enable(Long categoryID) {
        // TODO: Implement
    }

    @Override
    public void disable(Long categoryID) {
        // TODO: Implement
    }
}
