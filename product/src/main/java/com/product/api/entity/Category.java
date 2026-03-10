package com.product.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "category")
public class Category {

    @Id
    private Integer categoryID;
    private String category;
    private String tag;
    private Integer status;

    public Category(){}

    public Category(int categoryID,String category,String tag,int status){
        this.categoryID = categoryID;
        this.category = category;
        this.tag = tag;
        this.status = status;
    }

    public Integer getId() {return categoryID;}
    public void setId(Integer categoryID) {this.categoryID = categoryID;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}
    
    public String getTag() {return tag;}
    public void setTag(String tag) {this.tag = tag;}

    public Integer getStatus() {return status;}
    public void setStatus(Integer status) {this.status = status;}

}
