package com.product.exception;

import org.springframework.dao.DataAccessException;

public class DBAccessException extends RuntimeException {

    public DBAccessException(DataAccessException cause) {
        super(cause);
    }
}
