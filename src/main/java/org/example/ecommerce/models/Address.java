package org.example.ecommerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "addresses")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "street")
    @Size(min = 5, message = "Street name should be atleast 5 characters")
    private String street;

    @Column(name = "building_name")
    @Size(min = 5, message = "Building name should be atleast 5 characters")
    private String buildingName;

    @Size(min = 4, message = "City name should be atleast 4 characters")
    private String city;

    @Size(min = 5, message = "State name should be atleast 5 characters")
    private String state;

    @Size(min = 6, message = "Zip code should be atleast 6 characters")
    private String zip;

    @Size(min = 2, message = "Country name should be atleast 2 characters")
    private String country;

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

}
