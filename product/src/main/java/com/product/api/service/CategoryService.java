package com.product.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.in.DtoCategoryIn;
import com.product.api.entity.Category;

public interface CategoryService {

    ResponseEntity<List<Category>> findAll();

    ResponseEntity<List<Category>> findActive();

    ResponseEntity<String> create(DtoCategoryIn in);

    ResponseEntity<String> update(DtoCategoryIn in, Long categoryID);

    ResponseEntity<String> enable(Long categoryID);

    ResponseEntity<String> disable(Long categoryID);
}
