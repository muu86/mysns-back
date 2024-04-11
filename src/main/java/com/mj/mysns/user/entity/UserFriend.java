package com.mj.mysns.user.entity;

import com.mj.mysns.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserFriend extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "from_id")
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private User to;

    @Setter
    @Enumerated(value = EnumType.STRING)
    private FriendStatus status;

    public enum FriendStatus {
        UNKNOWN, REQUEST, ACCEPT, BLOCK
    }

    public UserFriend(User from, User to) {
        this.from = from;
        this.to = to;
        this.status = FriendStatus.UNKNOWN;
    }

    public UserFriend(User from, User to, FriendStatus status) {
        this.from = from;
        this.to = to;
        this.status = status;
    }
}
