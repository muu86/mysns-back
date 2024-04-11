package com.mj.mysns.post.entity;

import com.mj.mysns.common.BaseEntity;
import com.mj.mysns.common.file.File;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostFile extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    // 포스트에서 사진 나열 순서
    private Integer displayOrder;

    @Embedded
    private File file;

    @Builder
    private PostFile(Post post, Integer displayOrder, File file) {
        Assert.notNull(post, "post 가 null 입니다.");

        this.post = post;
        this.displayOrder = displayOrder;
        this.file = file;
    }
}
