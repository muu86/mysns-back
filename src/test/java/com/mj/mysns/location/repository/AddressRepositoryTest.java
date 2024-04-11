package com.mj.mysns.location.repository;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mj.mysns.location.entity.Address;
import java.util.List;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(properties = { "init.db.address = true" })
@Testcontainers
@Transactional
class AddressRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        DockerImageName.parse("postgis/postgis:16-master").asCompatibleSubstituteFor("postgres"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.targetUsername", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    AddressRepository addressRepository;

    @Test
    void findLegalAddressNear_부암동() {
        // 종로구 부암동
        double latitude = 37.59520617773402;
        double longitude = 126.96518955304883;
        Point<G2D> point = point(WGS84,
            g(longitude, latitude));

        List<Address> addressNear = addressRepository.findLegalAddressNear(latitude,
            longitude, 1, 0);
        assertEquals(addressNear.size(), 1);
        assertNotNull(addressNear.getFirst());
        Address address = addressNear.getFirst();
        assertEquals(address.getGungu(), "종로구");
        assertEquals(address.getEupmyundong(), "부암동");
    }

    @Test
    void findLegalAddressNear_금호동() {
        // 성동구 금호동1가
        double latitude = 37.55723481192904;
        double longitude = 127.02882302332466;
        Point<G2D> point = point(WGS84,
            g(longitude, latitude));

        List<Address> addressNear = addressRepository.findLegalAddressNear(latitude,
            longitude, 1, 0);
        assertEquals(addressNear.size(), 1);
        assertNotNull(addressNear.getFirst());
        Address address = addressNear.getFirst();
        assertEquals(address.getGungu(), "성동구");
        assertEquals(address.getEupmyundong(), "금호동1가");
    }
}