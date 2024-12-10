package org.example.ecommerce.service;

import org.example.ecommerce.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long id);

    List<AddressDTO> getAddressesByUserId();

    AddressDTO updateAddress(Long id, AddressDTO addressDTO);
}
