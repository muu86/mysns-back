package com.mj.mysns.batch.address;

import com.mj.mysns.location.entity.Address;
import com.mj.mysns.location.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@RequiredArgsConstructor
public class LegalAddressWriter implements ItemWriter<Address> {

    private final AddressRepository addressRepository;

    @Override
    public void write(Chunk<? extends Address> chunk) throws Exception {
        addressRepository.saveAll(chunk);
    }
}
