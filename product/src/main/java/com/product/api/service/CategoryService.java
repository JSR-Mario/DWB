package com.product.api.service;

import java.util.List;
import com.product.api.entity.Category;
import com.product.api.dto.DtoCategoryIn;
import com.product.api.dto.Response;

public interface CategoryService {
    public Response<List<Category>> findAll();

    public List<Category> findActive();

    public void create(DtoCategoryIn in);

    public void update(DtoCategoryIn in, Long categoryID);

    public void enable(Long categoryID);

    public void disable(Long categoryID);
}
