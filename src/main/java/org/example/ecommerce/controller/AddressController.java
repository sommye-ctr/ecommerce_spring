package org.example.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.ecommerce.payload.AddressDTO;
import org.example.ecommerce.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/addresses")
@RestController
@AllArgsConstructor
public class AddressController {

    private AddressService addressService;

    @PostMapping()
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO a = addressService.createAddress(addressDTO);
        return new ResponseEntity<>(a, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addressDTOS = addressService.getAllAddresses();
        return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) {
        AddressDTO addressDTO = addressService.getAddressById(id);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<AddressDTO>> getAddressByUserId() {
        List<AddressDTO> addressDTO = addressService.getAddressesByUserId();
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO a = addressService.updateAddress(id, addressDTO);
        return new ResponseEntity<>(a, HttpStatus.OK);
    }
}
