package com.mj.mysns.location.controller;

import com.mj.mysns.location.entity.Address;
import com.mj.mysns.location.service.LocationService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loc")
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/legal")
    public ResponseEntity<List<AddressResult>> getLegalAddresses() {
        List<Address> addresses = locationService.getLegalAddresses();
        List<AddressResult> results = addresses.stream()
            .map(a -> AddressResult.builder()
                .code(a.getCode())
                .sido(a.getSido())
                .gungu(a.getGungu())
                .eupmyundong(a.getEupmyundong())
                .li(a.getLi())
                .build())
            .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping(path = "/legal/near")
    public ResponseEntity<List<AddressResult>> getLegalAddressesNear(
        @RequestParam("latitude") Double latitude,
        @RequestParam("longitude") Double longitude,
        @RequestParam(value = "page", defaultValue = "5") int page,
        @RequestParam(value = "offset", defaultValue = "0") int offset) {

        List<Address> nearestAddress = locationService.getAddressNear(latitude, longitude, page, offset);
        List<AddressResult> results = nearestAddress.stream()
            .map(a -> AddressResult.builder()
                .code(a.getCode())
                .sido(a.getSido())
                .gungu(a.getGungu())
                .eupmyundong(a.getEupmyundong())
                .li(a.getLi())
                .build())
            .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

}
