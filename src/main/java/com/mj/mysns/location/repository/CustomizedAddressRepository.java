package com.mj.mysns.location.repository;

import com.mj.mysns.location.entity.Address;
import java.util.List;

public interface CustomizedAddressRepository {

    List<Address> findLegalAddressNear(double latitude, double longitude, int page,
        int offset);
}
