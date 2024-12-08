package org.example.ecommerce.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private long id;
    private String username;
    private String jwtToken;
    private List<String> roles;

    public UserInfoResponse(long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
