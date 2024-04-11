package com.mj.mysns.post.service;

import com.mj.mysns.post.dto.CommentDto;
import com.mj.mysns.post.dto.CreateComment;
import com.mj.mysns.post.dto.CreatePost;
import com.mj.mysns.post.dto.GetComment;
import com.mj.mysns.post.dto.GetPost;
import com.mj.mysns.post.dto.PostDto;
import com.mj.mysns.post.dto.UpdateComment;
import com.mj.mysns.post.dto.UpdatePost;
import java.util.List;

public interface PostService {

    void createPost(CreatePost createPost);

    void updatePost(UpdatePost postDto);

    List<PostDto> getPost(GetPost condition);

    List<PostDto> getUserPost(GetPost getPost);

    void addComment(CreateComment commentDto);

    void updateComment(UpdateComment commentDto);

    List<CommentDto> getComment(GetComment getComment);
}
