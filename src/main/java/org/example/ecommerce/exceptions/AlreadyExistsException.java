package org.example.ecommerce.exceptions;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends RuntimeException {
    private final String field;
    private final String value;

    public AlreadyExistsException(String field,String value) {
        super(String.format("%s with value %s already exists", field, value));
        this.field = field;
        this.value = value;
    }

    public AlreadyExistsException(String message, String field, String value) {
        super(message);
        this.field = field;
        this.value = value;
    }

}
