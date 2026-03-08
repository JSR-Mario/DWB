package com.product.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;

@Service
public class CategoryService {

    @Autowired
    RepoCategory repoCategory;

    public List<Category> getCategories(){
        return repoCategory.getCategories();
    }

}
