package com.mj.mysns.post.dto;

import com.mj.mysns.common.file.FileManager;
import com.mj.mysns.common.file.FileMapper;
import com.mj.mysns.location.dto.AddressMapper;
import com.mj.mysns.post.entity.Comment;
import com.mj.mysns.post.entity.Post;
import com.mj.mysns.user.dto.UserMapper;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = { UserMapper.class, FileMapper.class, AddressMapper.class })
public interface PostMapper {

    PostDto postToPostDto(Post post, @Context FileManager fileManager);

    List<PostDto> postToPostDto(List<Post> post, @Context FileManager fileManager);

    @Mapping(target = "createdAt", source="createdAt")
    @Mapping(target = "modifiedAt", source="modifiedAt")
    CommentDto commentToCommentDto(Comment comment);

    List<CommentDto> commentToCommentDto(List<Comment> comments);

}
