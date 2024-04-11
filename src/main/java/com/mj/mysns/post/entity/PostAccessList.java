package com.mj.mysns.post.entity;

import com.mj.mysns.common.BaseEntity;
import com.mj.mysns.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostAccessList extends BaseEntity {

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;
}
