package com.mj.mysns.post.entity;

import com.mj.mysns.common.BaseEntity;
import com.mj.mysns.common.file.File;
import com.mj.mysns.common.file.File.Status;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.post.dto.PostDto;
import com.mj.mysns.post.dto.UpdateComment;
import com.mj.mysns.post.entity.Comment.CommentBuilder;
import com.mj.mysns.post.entity.Comment.CommentStatus;
import com.mj.mysns.post.entity.PostFile.PostFileBuilder;
import com.mj.mysns.post.exception.CommentNotFoundException;
import com.mj.mysns.post.exception.PostNotFoundException;
import com.mj.mysns.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;

    @Size(max = 500)
    @Column(length = 500)
    @Setter
    private String content;

    // user 가 사진 순서를 바꿀 수 있도록 할 것
    @OrderBy("displayOrder ASC")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostFile> files;

    @ManyToOne
    @Setter
    private Address address;

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "post", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    @Setter
    private PostStatus status;

    @Enumerated(EnumType.STRING)
    private AccessType accessType;

    @OneToMany(mappedBy = "post", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<PostAccessList> accessList;

    @Builder
    private Post(User user, String content, List<PostFile> files, Address address,
        List<Comment> comments, PostStatus status, Set<PostAccessList> accessList) {
        Assert.notNull(user, "user 가 null 입니다.");

        this.user = user;
        this.content = content;
        this.files = Optional.ofNullable(files).orElse(new ArrayList<>());
        this.address = address;
        this.comments = Optional.ofNullable(comments).orElse(new ArrayList<>());
        this.status = Optional.ofNullable(status).orElse(PostStatus.ACTIVE);
        this.accessList = Optional.ofNullable(accessList).orElse(new HashSet<>());
    }

    public void addFile(Consumer<PostFileBuilder> builderFunction) {
        PostFileBuilder defaultBulder = PostFile.builder()
            .post(this)
            .displayOrder(0);

        builderFunction.accept(defaultBulder);
        PostFile postFile = defaultBulder.build();

        this.files.add(postFile);
    }

    public void addComment(Consumer<CommentBuilder> builderFunction) {
        CommentBuilder defaultBuilder = Comment.builder()
            .post(this);

        builderFunction.accept(defaultBuilder);
        Comment comment = defaultBuilder.build();

        this.comments.add(comment);
    }

    public void updateComment(UpdateComment commentDto) {
        if (!this.getId().equals(commentDto.postId())) {
            throw new PostNotFoundException();
        }
        Comment comment = this.comments.stream().filter(c -> c.getId().equals(commentDto.id()))
            .findFirst().orElseThrow(CommentNotFoundException::new);

        comment.updateFrom(commentDto);
    }

    public void changeAccessType(AccessType type) {
        this.accessType = type;
    }

    public void createAccessList(Collection<User> users) {
        this.accessList = users.stream()
            .map(user1 -> new PostAccessList(this, user1)).collect(Collectors.toSet());
    }

    public PostDto toDto() {
        return PostDto.builder()
            .id(getId())
            .user(user.toUserProfile())
            .content(content)
            .files(files.stream()
                .map(PostFile::getFile)
                .filter(f -> !Status.INACTIVE.equals(f.getStatus()))
                .map(File::toDto)
                .toList())
            .address(address.toDto())
            .comments(comments.stream()
                .filter(c -> !CommentStatus.INACTIVE.equals(c.getStatus()))
                .map(Comment::toDto).toList())
            .createdAt(getCreatedAt())
            .modifiedAt(getModifiedAt())
            .build();
    }

    public enum PostStatus {
        ACTIVE, INACTIVE, DELETED
    }

    public enum AccessType {
        PUBLIC, FRIEND, ACCESS_LIST, PRIVATE
    }
}
