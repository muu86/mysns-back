package com.mj.mysns.location.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.when;

import com.mj.mysns.location.entity.Address;
import com.mj.mysns.location.service.LocationService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LocationControllerTest {

    @MockBean
    LocationService locationService;

    @Autowired
    WebTestClient client;

    List<Address> addresses;

    @BeforeEach
    void setup() {
        List<Address> addresses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
        addresses.add(Address.builder()
            .code(String.valueOf(i)).build());
        }
        this.addresses = addresses;
    }

    @Test
    void getAllLegalAddresses_ok() {
        given(locationService.getLegalAddresses()).willReturn(this.addresses);

        client.get().uri("/loc/legal")
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.length()").isEqualTo(10);

        verify(locationService).getLegalAddresses();
    }

    @Test
    void getNearest_ok() {
        double latitude = 37.59554172008826;
        double longitude = 126.96567699902982;
        int page = 1;
        int offset = 10;
        List<Address> addresses = new ArrayList<>();

        when(locationService.getAddressNear(latitude, longitude, page, offset))
            .thenReturn(addresses);

        client.get().uri(uri -> uri
            .path("/loc/legal/near")
            .queryParam("latitude", latitude)
            .queryParam("longitude", longitude)
            .queryParam("page", page)
            .queryParam("offset", offset)
            .build())
            .exchange()
            .expectStatus().isOk();
    }
}