package com.mj.mysns.user.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserAddressId implements Serializable {

    private Long userId;


    private Long legalAddressId;

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
        UserAddressId that = (UserAddressId) o;
        return getUserId() != null && Objects.equals(getUserId(), that.getUserId())
               && getLegalAddressId() != null && Objects.equals(getLegalAddressId(),
            that.getLegalAddressId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(userId, legalAddressId);
    }
}
