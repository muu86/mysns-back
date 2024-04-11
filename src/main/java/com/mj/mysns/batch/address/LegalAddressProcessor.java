package com.mj.mysns.batch.address;

import com.mj.mysns.location.entity.Address;
import org.springframework.batch.item.ItemProcessor;

public class LegalAddressProcessor implements ItemProcessor<Address, Address> {

    // 주소 데이터에서 삭제된 데이터인 경우 제외시킨다.
    @Override
    public Address process(Address item) {
        if (item.getMetadata().getMetaDeletedAt() != null) return null;

        return item;
    }
}
