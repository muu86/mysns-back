package com.mj.mysns.post.dto;

import com.mj.mysns.common.file.FileDto;
import com.mj.mysns.common.file.FileManager;
import com.mj.mysns.common.file.FileMapper;
import com.mj.mysns.location.dto.AddressDto;
import com.mj.mysns.location.dto.AddressMapper;
import com.mj.mysns.post.entity.Comment;
import com.mj.mysns.post.entity.Post;
import com.mj.mysns.post.entity.PostFile;
import com.mj.mysns.user.dto.UserMapper;
import com.mj.mysns.user.dto.UserProfile;
import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.entity.UserAddress;
import com.mj.mysns.user.entity.UserFile;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-12T03:29:59+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public PostDto postToPostDto(Post post, FileManager fileManager) {
        if ( post == null ) {
            return null;
        }

        PostDto.PostDtoBuilder postDto = PostDto.builder();

        postDto.id( post.getId() );
        postDto.user( userMapper.userToUserProfileDto( post.getUser(), fileManager ) );
        postDto.content( post.getContent() );
        postDto.files( postFileListToFileDtoList( post.getFiles(), fileManager ) );
        postDto.address( addressMapper.addressToAddressDto( post.getAddress() ) );
        postDto.comments( commentToCommentDto( post.getComments() ) );
        postDto.createdAt( post.getCreatedAt() );
        postDto.modifiedAt( post.getModifiedAt() );

        return postDto.build();
    }

    @Override
    public List<PostDto> postToPostDto(List<Post> post, FileManager fileManager) {
        if ( post == null ) {
            return null;
        }

        List<PostDto> list = new ArrayList<PostDto>( post.size() );
        for ( Post post1 : post ) {
            list.add( postToPostDto( post1, fileManager ) );
        }

        return list;
    }

    @Override
    public CommentDto commentToCommentDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto.CommentDtoBuilder commentDto = CommentDto.builder();

        commentDto.createdAt( comment.getCreatedAt() );
        commentDto.modifiedAt( comment.getModifiedAt() );
        commentDto.id( comment.getId() );
        commentDto.user( userToUserProfile( comment.getUser() ) );
        commentDto.content( comment.getContent() );
        commentDto.status( comment.getStatus() );

        return commentDto.build();
    }

    @Override
    public List<CommentDto> commentToCommentDto(List<Comment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentDto> list = new ArrayList<CommentDto>( comments.size() );
        for ( Comment comment : comments ) {
            list.add( commentToCommentDto( comment ) );
        }

        return list;
    }

    protected List<FileDto> postFileListToFileDtoList(List<PostFile> list, FileManager fileManager) {
        if ( list == null ) {
            return null;
        }

        List<FileDto> list1 = new ArrayList<FileDto>( list.size() );
        for ( PostFile postFile : list ) {
            list1.add( fileMapper.postFileToFileDto( postFile ) );
        }

        return list1;
    }

    protected List<FileDto> userFileListToFileDtoList(List<UserFile> list) {
        if ( list == null ) {
            return null;
        }

        List<FileDto> list1 = new ArrayList<FileDto>( list.size() );
        for ( UserFile userFile : list ) {
            list1.add( fileMapper.userFileToFileDto( userFile ) );
        }

        return list1;
    }

    protected Set<AddressDto> userAddressSetToAddressDtoSet(Set<UserAddress> set) {
        if ( set == null ) {
            return null;
        }

        Set<AddressDto> set1 = new LinkedHashSet<AddressDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( UserAddress userAddress : set ) {
            set1.add( addressMapper.userAddressToAddressDto( userAddress ) );
        }

        return set1;
    }

    protected UserProfile userToUserProfile(User user) {
        if ( user == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder userProfile = UserProfile.builder();

        userProfile.username( user.getUsername() );
        userProfile.babyMonths( user.getBabyMonths() );
        userProfile.content( user.getContent() );
        userProfile.files( userFileListToFileDtoList( user.getFiles() ) );
        userProfile.addresses( userAddressSetToAddressDtoSet( user.getAddresses() ) );

        return userProfile.build();
    }
}
