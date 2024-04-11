package com.mj.mysns.post.controller;

import com.mj.mysns.post.dto.CommentDto;
import com.mj.mysns.post.dto.CreateComment;
import com.mj.mysns.post.dto.CreatePost;
import com.mj.mysns.post.dto.GetComment;
import com.mj.mysns.post.dto.GetPost;
import com.mj.mysns.post.dto.PostDto;
import com.mj.mysns.post.dto.PostMapper;
import com.mj.mysns.post.dto.UpdatePost;
import com.mj.mysns.post.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    private final PostMapper postMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostPayload payload) {

        if (payload.files().stream()
            .anyMatch(f -> !f.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"))) {
            return ResponseEntity.badRequest().build();
        }

        CreatePost createPost = CreatePost.builder()
            .username(payload.username())
            .content(payload.content())
            .keys(payload.files())
//            .files(payload.files())
            .build();
        postService.createPost(createPost);

        return ResponseEntity.ok().build();
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(@Valid UpdatePostPayload payload) {

        UpdatePost postDto = UpdatePost.builder()
            .postId(payload.postId())
            .username(payload.username())
            .content(payload.content())
            .files(payload.files()).build();
        postService.updatePost(postDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getPost(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) Double latitude,
        @RequestParam(required = false) Double longitude,
        @RequestParam(defaultValue = "0") int offset) {

        GetPost condition = GetPost.builder()
            .targetUsername(username)
            .latitude(latitude)
            .longitude(longitude)
            .offset(offset)
            .build();
        List<PostDto> post = postService.getPost(condition);
        log.info("offset {}", offset);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<PostDto>> getUserPost(@PathVariable String username) {
        List<PostDto> posts = postService.getUserPost(
            GetPost.builder().username(username).build());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/comment")
    public ResponseEntity<List<CommentDto>> getComment(
        @RequestParam Long postId) {

        return ResponseEntity.ok(postService.getComment(new GetComment(postId)));
    }

    @PostMapping("/comment")
    public ResponseEntity<?> addComment(@Valid @RequestBody AddCommentPayload payload) {
        PostDto post = PostDto.builder()
            .id(payload.postId()).build();
        CreateComment commentDto = CreateComment.builder()
            .username(payload.username())
            .post(post)
            .content(payload.content())
            .build();

        postService.addComment(commentDto);

        return ResponseEntity.ok().build();
    }
}
