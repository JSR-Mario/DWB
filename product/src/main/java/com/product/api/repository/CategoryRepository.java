package com.product.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.product.api.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAllByOrderByCategoryAsc();

    // usamos la entidad en vez de sql puro
    // @Query("SELECT c FROM Category c WHERE c.status = 1 ORDER BY c.category")

    // ahora con JPA
    List<Category> findByStatusOrderByCategoryAsc(Integer status);

    // sin JPA
    // @Modifying(clearAutomatically = true, flushAutomatically = true)
    // @Query(value = "INSERT INTO category(category,tag,status)
    // VALUES(:category,:tag,1)", nativeQuery = true)
    // void create(@Param("category") String category, @Param("tag") String tag);

    // con entidades, aqui si conviene usar query para evitar doble consulta
    @Modifying
    @Query("UPDATE Category c SET c.status = :status WHERE c.categoryID = :id")
    void updateStatus(Long id, Integer status);
}