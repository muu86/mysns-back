package com.mj.mysns.user.entity;

import com.mj.mysns.common.BaseEntity;
import com.mj.mysns.common.file.File;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserFile extends BaseEntity {

    @ManyToOne
    private User user;

    @Embedded
    private File file;

    @Builder
    public UserFile(User user, File file) {
        Assert.notNull(user, "user 가 null 입니다.");

        this.user = user;
        this.file = file;
    }
}
