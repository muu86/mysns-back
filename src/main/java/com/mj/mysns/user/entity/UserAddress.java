package com.mj.mysns.user.entity;

import com.mj.mysns.common.BaseEntity;
import com.mj.mysns.location.entity.Address;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.util.Assert;

@Entity
@NoArgsConstructor
@Getter
public class UserAddress extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public UserAddress(User user, Address address, Status status) {
        Assert.notNull(user, "user 가 null 입니다");
        Assert.notNull(address, "address 가 null 입니다");
        this.user = user;
        this.address = address;
        this.status = Optional.ofNullable(status).orElse(Status.ACTIVE);
    }

    public enum Status {
        MAIN, ACTIVE, INACTIVE
    }

    // TODO equals 재정의 필요

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass = o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer()
            .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer()
            .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        UserAddress that = (UserAddress) o;
        return user != null && Objects.equals(user.getId(), that.user.getId()) && address != null
               && Objects.equals(address.getId(), that.address.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer()
            .getPersistentClass().hashCode() : getClass().hashCode();
    }
}
