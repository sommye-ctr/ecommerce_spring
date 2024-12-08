package org.example.ecommerce.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private long id;
    private String username;
    private List<String> roles;
}
