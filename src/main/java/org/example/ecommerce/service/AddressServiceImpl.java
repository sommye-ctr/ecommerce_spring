package org.example.ecommerce.service;

import lombok.AllArgsConstructor;
import org.example.ecommerce.models.Address;
import org.example.ecommerce.models.User;
import org.example.ecommerce.payload.AddressDTO;
import org.example.ecommerce.repositories.AddressRepository;
import org.example.ecommerce.utils.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private AuthUtils authUtils;
    private ModelMapper modelMapper;
    private AddressRepository addressRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        User user = authUtils.loggedInUser();
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);

        address = addressRepository.save(address);
        return modelMapper.map(address, AddressDTO.class);
    }
}
