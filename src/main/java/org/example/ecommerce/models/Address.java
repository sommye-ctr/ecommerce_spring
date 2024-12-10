package org.example.ecommerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotNull
    private String street;

    @Column(name = "building_name")
    @Size(min = 5, message = "Building name should be atleast 5 characters")
    @NotNull
    private String buildingName;

    @Size(min = 4, message = "City name should be atleast 4 characters")
    @NotNull
    private String city;

    @Size(min = 5, message = "State name should be atleast 5 characters")
    @NotNull
    private String state;

    @Size(min = 6, message = "Zip code should be atleast 6 characters")
    @NotNull
    private String zip;

    @Size(min = 2, message = "Country name should be atleast 2 characters")
    @NotNull
    private String country;

    @ManyToOne()
    private User user;

    public void update(Address address){
        this.id = address.getId() == 0 ? this.id : address.getId();
        this.state = address.getState() == null ? this.state : address.getState();
        this.buildingName = address.getBuildingName() == null ? this.buildingName : address.getBuildingName();
        this.city = address.getCity() == null ? this.city : address.getCity();
        this.zip = address.getZip() == null ? this.zip : address.getZip();
        this.street = address.getStreet() == null ? this.street : address.getStreet();
        this.country = address.getCountry() == null ? this.country : address.getCountry();
    }

}
