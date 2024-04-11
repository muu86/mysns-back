package com.mj.mysns.location.repository;

import com.mj.mysns.location.entity.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long>, CustomizedAddressRepository {

    Optional<Address> findByCode(String code);

    List<Address> findByCodeIn(List<String> codes);

    Optional<Address> findBySidoAndGunguAndEupmyundong(String sido, String gungu, String eupmyundong);
}
