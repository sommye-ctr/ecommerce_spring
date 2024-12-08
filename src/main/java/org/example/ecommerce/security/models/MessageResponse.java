package org.example.ecommerce.security.models;

import lombok.Data;
import lombok.Getter;

@Data
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}