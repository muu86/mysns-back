package com.mj.mysns.post.service;

import com.mj.mysns.common.file.File;
import com.mj.mysns.common.file.FileLocation;
import com.mj.mysns.common.file.FileLocation.FileLocationType;
import com.mj.mysns.common.file.FileManager;
import com.mj.mysns.common.file.FileUrl;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.post.dto.CommentDto;
import com.mj.mysns.post.dto.CreateComment;
import com.mj.mysns.post.dto.CreatePost;
import com.mj.mysns.post.dto.GetComment;
import com.mj.mysns.post.dto.GetPost;
import com.mj.mysns.post.dto.PostDto;
import com.mj.mysns.post.dto.PostMapper;
import com.mj.mysns.post.dto.UpdateComment;
import com.mj.mysns.post.dto.UpdatePost;
import com.mj.mysns.post.entity.Comment;
import com.mj.mysns.post.entity.Post;
import com.mj.mysns.post.entity.Post.PostBuilder;
import com.mj.mysns.post.exception.PostNotFoundException;
import com.mj.mysns.post.repository.PostRepository;
import com.mj.mysns.user.dto.UserProfile;
import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.entity.UserAddress;
import com.mj.mysns.user.entity.UserAddress.Status;
import com.mj.mysns.user.exception.UserNotFoundException;
import com.mj.mysns.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final int commentFirstLimit;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final FileManager fileManager;

    private final PostMapper postMapper;

    public PostServiceImpl(@Value("${mysns.comment-first-limit}") int COMMENT_FIRST_LIMIT, PostRepository postRepository,
        UserRepository userRepository, FileManager fileManager, PostMapper postMapper) {
        this.commentFirstLimit = COMMENT_FIRST_LIMIT;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileManager = fileManager;
        this.postMapper = postMapper;
    }

    @Override
    @Transactional
    public void createPost(CreatePost createPost) {
        // user 정보
        User user = userRepository.findByUsername(createPost.username())
            .orElseThrow(UserNotFoundException::new);

        PostBuilder builder = Post.builder();
        builder.user(user);
        builder.content(createPost.content());
        builder.address(getUserAddress(user, createPost.addressCode()));
        Post post = builder.build();

        addPostFile(createPost, post);

        postRepository.save(post);
    }

    @Override
    @Transactional
    public void updatePost(UpdatePost updatePost) {
        Post post = postRepository.findById(updatePost.postId()).orElseThrow(PostNotFoundException::new);

        // 일단 content 만 수정
        post.setContent(updatePost.content());
        postRepository.save(post);
    }

    @Override
    public List<PostDto> getPost(GetPost getPost) {
        // TODO
        // 서비스 범위 체크하는 로직 필요
//        if (!checkLatLong(latitude, longitude)) {
//            //..
//        }

        List<Post> posts = postRepository.findPost(getPost);
        List<PostDto> dtos = posts.stream().map(Post::toDto).toList();

        setFileUrl(dtos);
        return dtos;
    }

    @Override
    public List<PostDto> getUserPost(GetPost getPost) {
        List<Post> posts = postRepository.findUserPost(getPost);
        List<PostDto> dtos = posts.stream().map(Post::toDto).toList();
        setFileUrl(dtos);
        return dtos;
    }

    @Override
    @Transactional
    public void addComment(CreateComment createComment) {
        Post post = postRepository.findById(createComment.post().getId())
            .orElseThrow(PostNotFoundException::new);
        User user = userRepository.findByUsername(createComment.username())
            .orElseThrow(UserNotFoundException::new);

        post.addComment(builder -> builder
            .user(user)
            .content(createComment.content()));

        postRepository.save(post);
    }

    @Override
    @Transactional
    public void updateComment(UpdateComment updateComment) {
        Post post = postRepository.findById(updateComment.id())
            .orElseThrow(PostNotFoundException::new);
        post.updateComment(updateComment);
        postRepository.save(post);
    }

    @Override
    public List<CommentDto> getComment(GetComment getComment) {
        postRepository.findById(getComment.postId()).orElseThrow(PostNotFoundException::new);
        List<Comment> comments = postRepository.findCommentByPostId(getComment);
        List<CommentDto> dtos = comments.stream().map(Comment::toDto).toList();
        setUserFileUrl(dtos.stream().map(CommentDto::getUser).toList());
        return dtos;
    }

    private void addPostFile(CreatePost createPost, Post post) {
//        if (createPost.files() != null) {
//            List<File> files = createFile(createPost.files());
//            files.forEach(f -> post.addFile(b -> b.file(f)));
        if (createPost.keys() != null) {
            createPost.keys().stream()
                .map(k -> new File(new FileLocation(k, FileLocationType.S3)))
                .forEach(f -> post.addFile(b -> b.file(f)));
        }
    }

    private void setFileUrl(List<PostDto> dtos) {
//        setPostFileUrl(dtos);
        setPostFileUrlV2(dtos);
        // post 작성자
        setUserFileUrl(dtos.stream().map(PostDto::getUser).toList());
        // comment 단 사람
        setUserFileUrl(dtos.stream()
            .flatMap(d -> d.getComments().stream())
            .map(CommentDto::getUser)
            .toList());
    }

    private boolean checkLatLong(double latitude, double longitude) {
        // TODO
        // 범위가 맞는지 체크하는 기능이 필요
        return true;
    }

    private Address getUserAddress(User user, String addressCode) {
        if (addressCode == null) {
            return getUserActiveAddress(user);
        }

        return getUserSelectedAddress(user, addressCode);
    }

    private Address getUserActiveAddress(User user) {
        for (UserAddress address : user.getAddresses()) {
            if (address.getStatus().equals(Status.MAIN)) {
                return address.getAddress();
            }

            if (address.getStatus().equals(Status.ACTIVE)) {
                return address.getAddress();
            }
        }
        return null;
    }

    private Address getUserSelectedAddress(User user, String addressCode) {
        if (addressCode != null) {
            Optional<Address> legalAddress = user.getAddresses().stream()
                .filter(ua -> addressCode.equals(ua.getAddress().getCode()))
                .findFirst()
                .map(UserAddress::getAddress);
            if (legalAddress.isPresent()) {
                return legalAddress.get();
            }
        }
        return null;
    }

    private List<File> createFile(List<? extends MultipartFile> files) {
        if (files == null) return null;

        return fileManager.saveFile(files)
            .stream()
            .map(File::new)
            .toList();
    }

    private void setUserFileUrl(List<UserProfile> users) {
//        for (UserProfile user : users) {
//            user.getFiles().forEach(f -> {
//                String url = fileManager.getFileUrl(f.getLocation());
//                f.setUrl(url);
//            });
//        }

        for (UserProfile user : users) {
            user.getFiles().forEach(f -> {
                FileUrl url = fileManager.getFileUrl(f.getLocation());
                f.setUrl(url);
            });
        }
    }

    private void setPostFileUrlV2(List<PostDto> dtos) {
        for (PostDto dto : dtos) {
            dto.getFiles().forEach(f -> {
                FileUrl fileUrlV2 = fileManager.getFileUrl(f.getLocation());
                f.setUrl(fileUrlV2);
            });
        }
    }
}
