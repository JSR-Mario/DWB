package com.product.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "category")
public class Category {

    @Id
    private Integer categoryID;
    private String name;

    public Category(){}

    public Integer getId() {
        return categoryID;
    }

    public void setId(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
