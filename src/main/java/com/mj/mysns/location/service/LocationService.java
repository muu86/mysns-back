package com.mj.mysns.location.service;

import com.mj.mysns.location.entity.Address;
import com.mj.mysns.location.repository.AddressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor

@Service
public class LocationService {

    private final AddressRepository addressRepository;

    public List<Address> getAddressNear(double latitude, double longitude, int page,
        int offset) {
        return addressRepository.findLegalAddressNear(latitude, longitude, page, offset);
    }

    public List<Address> getLegalAddresses() {
        return addressRepository.findAll();
    }

    public List<Address> getLegalAddressesByCodeIn(List<String> codes) {
        return addressRepository.findByCodeIn(codes);
    }
}
