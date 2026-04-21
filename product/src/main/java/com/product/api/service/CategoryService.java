package com.product.api.service;

import java.util.List;
import com.product.api.entity.Category;
import com.product.api.dto.in.DtoCategoryIn;

public interface CategoryService {
    List<Category> findAll();

    List<Category> findActive();

    void create(DtoCategoryIn in);

    void update(DtoCategoryIn in, Long categoryID);

    void enable(Long categoryID);

    void disable(Long categoryID);
}
