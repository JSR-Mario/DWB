package com.product.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.product.api.entity.Product;
import com.product.api.dto.out.DtoProductOut;

@Repository
public interface RepoProduct extends JpaRepository<Product, Integer> {

	@Query("SELECT new com.product.api.dto.out.DtoProductOut("
			+ "p.product_id, p.gtin, p.product, p.description, p.price, p.stock, p.status, "
			+ "p.category_id, c.category) "
			+ "FROM Product p "
			+ "JOIN Category c ON p.category_id = c.categoryID "
			+ "WHERE p.product_id = :id")
	DtoProductOut getProduct(@Param("id") Integer id);

}
