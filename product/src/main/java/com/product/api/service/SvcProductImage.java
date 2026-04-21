package com.product.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.entity.ProductImage;

public interface SvcProductImage {

    public ResponseEntity<List<ProductImage>> getProductImages(Integer productId);
    public ResponseEntity<String> createProductImage(Integer productId, DtoProductImageIn in);
    public ResponseEntity<String> deleteProductImage(Integer productId, Integer productImageId);
}
