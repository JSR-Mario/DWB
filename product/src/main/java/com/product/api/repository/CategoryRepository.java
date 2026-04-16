package com.product.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.product.api.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByOrderByCategoryAsc();

    List<Category> findByStatusOrderByCategoryAsc(Integer status);

    boolean existsByCategory(String category);

    boolean existsByCategoryAndCategoryIDNot(String category, Long categoryID);
}