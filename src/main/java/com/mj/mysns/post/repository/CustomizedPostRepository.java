package com.mj.mysns.post.repository;

import com.mj.mysns.post.dto.GetComment;
import com.mj.mysns.post.dto.GetPost;
import com.mj.mysns.post.entity.Comment;
import com.mj.mysns.post.entity.Post;
import java.util.List;
import java.util.Map;

public interface CustomizedPostRepository {

    List<Post> findPost(GetPost condition);

    List<Post> findUserPost(GetPost condition);

    List<Comment> findCommentByPostId(GetComment condition);

    Map<Post, List<Comment>> findCommentByPost(List<Post> posts, int limit);
}
