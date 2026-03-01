package com.product.model;

public class Category{
    private int categoryID;
    private String name;
    private String tag;
    private int status;

    public Category(int categoryID,String name,String tag,int status){
        this.categoryID = categoryID;
        this.name = name;
        this.tag = tag;
        this.status = status;
    }

    public int getID(){return categoryID;}
    public String getName(){return name;}
    public String getTag(){return tag;}
    public int getStatus(){return status;}
}
