package org.example.ecommerce.service;

import lombok.AllArgsConstructor;
import org.example.ecommerce.exceptions.ResourceNotFoundException;
import org.example.ecommerce.models.Address;
import org.example.ecommerce.models.User;
import org.example.ecommerce.payload.AddressDTO;
import org.example.ecommerce.repositories.AddressRepository;
import org.example.ecommerce.utils.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();

        return addresses
                .stream()
                .map(a -> modelMapper.map(a, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", id));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddressesByUserId() {
        User user = authUtils.loggedInUser();

        return user.getAddresses()
                .stream().map(
                        address -> modelMapper.map(address, AddressDTO.class)
                )
                .toList();
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", id));

        address.update(modelMapper.map(addressDTO, Address.class));
        address = addressRepository.save(address);
        return modelMapper.map(address, AddressDTO.class);
    }
}
