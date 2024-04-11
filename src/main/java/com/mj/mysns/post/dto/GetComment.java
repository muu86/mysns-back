package com.mj.mysns.post.dto;

public record GetComment(
    Long postId

) {

    public GetComment(Long postId) {
        this.postId = postId;
    }
}
