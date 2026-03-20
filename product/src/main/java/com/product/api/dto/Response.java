package com.product.api.dto;

public class Response<T> {
    private T data;
    private String message;
    private Boolean success;

    public Response() {
    }

    public Response(T data) {
        this.data = data;
        this.success = true;
    }

    public Response(T data, String message) {
        this.data = data;
        this.message = message;
        this.success = true;
    }

    // Getters and Setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
