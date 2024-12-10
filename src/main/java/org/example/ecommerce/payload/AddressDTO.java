package org.example.ecommerce.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private long id;
    private String street;
    private String buildingName;
    private String city;
    private String state;
    private String zip;
    private String country;

}
