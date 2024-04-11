package com.mj.mysns.user.entity;

import static com.mj.mysns.user.entity.UserAddress.Status.INACTIVE;

import com.mj.mysns.common.BaseEntity;
import com.mj.mysns.common.file.File;
import com.mj.mysns.common.file.File.Status;
import com.mj.mysns.common.file.FileLocation;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.post.entity.Comment;
import com.mj.mysns.user.dto.UserProfile;
import com.mj.mysns.user.entity.UserFriend.FriendStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"issuer", "subject"})
})
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Size(min = 2, max = 20)
    @Column(nullable = false, unique = true)
    @Setter
    private String username;

    @Max(300)
    @Setter
    private Integer babyMonths;

    @Size(max = 500)
    @Setter
    private String content;

    @OneToMany(mappedBy = "to", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<UserFriend> relations;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<UserFile> files;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<UserAddress> addresses;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Comment> comments;

    @Embedded
    private Claims claims;

    @Builder
    public User(String username, Integer babyMonths, String content, List<UserFriend> relations,
        List<UserFile> files, Set<UserAddress> addresses, List<Comment> comments, Claims claims) {

        this.username = username;
        this.babyMonths = babyMonths;
        this.content = content;
        this.relations = Optional.ofNullable(relations).orElse(new ArrayList<>());
        this.files = Optional.ofNullable(files).orElse(new ArrayList<>());
        this.addresses = Optional.ofNullable(addresses).orElse(new HashSet<>());
        this.comments = Optional.ofNullable(comments).orElse(new ArrayList<>());
        this.claims = claims;
    }

    public void addUserAddress(UserAddress userAddress) {
        this.addresses.add(userAddress);
    }

    public void addUserFile(FileLocation fileLocation, Status status) {
        UserFile userFile = new UserFile(this, new File(fileLocation, status));
        this.files.add(userFile);
    }

    public void requestFriend(User to) {
        this.relations.add(new UserFriend(this, to, FriendStatus.REQUEST));
        to.getRelations().add(new UserFriend(to, this, FriendStatus.UNKNOWN));
    }

    public void acceptFriend(User from) {
        this.relations.stream()
            .filter(relation -> relation.getFrom().equals(this) && relation.getTo().equals(from))
            .forEach(relation -> relation.setStatus(FriendStatus.ACCEPT));
        from.getRelations().stream()
            .filter(relation -> relation.getFrom().equals(from) && relation.getTo().equals(this))
            .forEach(relation -> relation.setStatus(FriendStatus.ACCEPT));
    }

    public UserProfile toUserProfile() {
        return UserProfile.builder()
            .username(username)
            .babyMonths(babyMonths)
            .content(content)
            .files(files.stream()
                .map(UserFile::getFile)
                .filter(f -> !Status.INACTIVE.equals(f.getStatus()))
                .map(File::toDto)
                .toList())
            .addresses(addresses.stream()
                .filter(ua -> !INACTIVE.equals(ua.getStatus()))
                .map(UserAddress::getAddress)
                .map(Address::toDto)
                .collect(Collectors.toSet()))
            .build();
    }
}
