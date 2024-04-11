package com.mj.mysns.any;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mj.mysns.common.file.File;
import com.mj.mysns.common.file.File.Status;
import com.mj.mysns.common.file.FileDto;
import com.mj.mysns.common.file.FileLocation;
import com.mj.mysns.common.file.FileLocation.FileLocationType;
import com.mj.mysns.common.file.FileManager;
import com.mj.mysns.common.file.FileMapper;
import com.mj.mysns.location.dto.AddressMapper;
import com.mj.mysns.location.entity.Address;
import com.mj.mysns.post.dto.PostDto;
import com.mj.mysns.post.dto.PostMapper;
import com.mj.mysns.post.entity.Post;
import com.mj.mysns.post.entity.Post.PostStatus;
import com.mj.mysns.user.dto.UserMapper;
import com.mj.mysns.user.dto.UserProfile;
import com.mj.mysns.user.entity.User;
import com.mj.mysns.user.entity.UserAddress;
import com.mj.mysns.user.entity.UserFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class MapperTest {

    @Autowired
    FileMapper fileMapper;
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    PostMapper postMapper;

    @Autowired
    FileManager fileManager;

    @Test
    void userFileToFileDto() {
        File file = new File(new FileLocation("key", FileLocationType.S3));
        User user = User.builder().username("user").build();
        UserFile userFile = UserFile.builder().file(file).user(user).build();
        FileDto fileDto = fileMapper.userFileToFileDto(userFile);
        System.out.println(fileDto);
    }

//    @Test
//    void legalAddressToDto() {
//        LegalAddress legalAddress = LegalAddress.builder()
//            .code("123")
//            .sido("서울")
//            .gungu("종로구")
//            .eupmyundong("동동")
//            .li("리리")
//            .build();
//        ReflectionTestUtils.setField(legalAddress, "id", 1L);
//        LegalAddressDto dto = legalAddressMapper.legalAddressToDto(legalAddress);
//        System.out.println(dto);
//    }

    @Test
    void userToUserProfileDto() {
        Address address = Address.builder()
            .code("123")
            .sido("서울")
            .gungu("종로구")
            .eupmyundong("동동")
            .li("리리")
            .build();
        ReflectionTestUtils.setField(address, "id", 1L);

        User user = User.builder()
            .username("user")
            .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        UserAddress userAddress = UserAddress.builder()
            .user(user)
            .address(address)
            .build();

        user.addUserAddress(userAddress);
        user.addUserFile(new FileLocation("key", FileLocationType.S3), Status.ACTIVE);

        UserProfile userProfile = userMapper.userToUserProfileDto(user, fileManager);
//        System.out.println(userProfile);
        assertEquals(1, userProfile.getAddresses().size());
        assertEquals(1, userProfile.getFiles().size());
    }

    @Test
    void postToPostDto() {
        Address address = Address.builder()
            .code("123")
            .sido("서울")
            .gungu("종로구")
            .eupmyundong("동동")
            .li("리리")
            .build();
        ReflectionTestUtils.setField(address, "id", 1L);

        User user = User.builder()
            .username("user")
            .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        UserAddress userAddress = UserAddress.builder()
            .user(user)
            .address(address)
            .build();

        user.addUserAddress(userAddress);
        user.addUserFile(new FileLocation("key", FileLocationType.S3), Status.ACTIVE);

        Post post = Post.builder()
            .user(user)
            .content("content")
            .status(PostStatus.ACTIVE)
            .address(address)
            .build();
        ReflectionTestUtils.setField(post, "id", 1L);

        // post file
        post.addFile(b -> b.file(new File(new FileLocation("key", FileLocationType.S3))));
        post.addComment(b -> b.content("hi").user(user));

        PostDto postDto = postMapper.postToPostDto(post, fileManager);
        assertEquals(1L, postDto.getId());
        assertEquals("user", postDto.getUser().getUsername());
        assertEquals(1, postDto.getFiles().size());
        assertEquals("동동", postDto.getAddress().eupmyundong());
        assertEquals("content", postDto.getContent());
    }
}


