package com.mj.mysns.user.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UserAddressTest {

    @Test
    void constructor_NullUser_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
             UserAddress.builder().build();
        });
    }

    @Test
    void constructor_NullAddress_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserAddress.builder()
                .user(User.builder().build())
                .build();
        });
    }
}