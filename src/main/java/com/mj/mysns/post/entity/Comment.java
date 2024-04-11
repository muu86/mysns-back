package com.mj.mysns.post.entity;

import com.mj.mysns.common.BaseEntity;
import com.mj.mysns.post.dto.CommentDto;
import com.mj.mysns.post.dto.UpdateComment;
import com.mj.mysns.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.util.Assert;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends BaseEntity {

    @ManyToOne
    private Post post;

    @ManyToOne
    @Setter
    private User user;

    @Size(max = 500)
    private String content;

    @Enumerated(EnumType.STRING)
    @Setter
    private CommentStatus status;

    @Builder
    public Comment(Post post, User user, String content, CommentStatus status) {
        Assert.notNull(user, "user 가 null 입니다.");
        Assert.notNull(post, "post 가 null 입니다.");

        this.post = post;
        this.user = user;
        this.content = content;
        this.status = Optional.ofNullable(status).orElse(CommentStatus.ACTIVE);
    }

    protected void updateFrom(UpdateComment dto) {

        if (this.getId().equals(dto.id())) {
            return;
        }

        if (dto.content() != null) {
            this.content = dto.content();
        }
        if (dto.status() != null) {
            this.status = dto.status();
        }
    }

    public enum CommentStatus {
        ACTIVE, INACTIVE, DELETED
    }

    public CommentDto toDto() {
        return CommentDto.builder()
            .id(getId())
            .user(user.toUserProfile())
            .content(content)
            .status(status)
            .createdAt(getCreatedAt())
            .modifiedAt(getModifiedAt())
            .build();
    }

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
        Comment that = (Comment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer()
            .getPersistentClass().hashCode() : getClass().hashCode();
    }

}
