package com.mj.mysns.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyInserters.fromMultipartData;

import com.mj.mysns.user.dto.UserDto;
import com.mj.mysns.user.dto.UserProfile;
import com.mj.mysns.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    WebTestClient client;

    @Autowired MessageSource ms;

    @Test
    void getUserProfile_WithoutUsername_400status() {
        client.get().uri("/user/profile")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void getUserProfile_NotExistingUsername_400status() {
        UserDto userDto = UserDto.builder().username("test").build();
        when(userService.getUserProfileByUsername(userDto)).thenReturn(Optional.empty());
        client.get().uri("/user/profile=targetUsername=test")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void getUserProfile_EmptyUsername_400status() {
        client.get().uri("/user/profile?targetUsername=")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void getUserProfile_ExistingUsername_200Status() {
        UserDto userDto = UserDto.builder().username("test").build();

        when(userService.getUserProfileByUsername(userDto)).thenReturn(Optional.of(
            UserProfile.builder()
            .username("test")
            .babyMonths(10)
            .content("소개글")
            .build()));

        client.get().uri("/user/profile?targetUsername=test")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.targetUsername").isEqualTo("test");

        verify(userService, times(1)).getUserProfileByUsername(userDto);
    }

    @Test
    void checkUserExistsByIssuerAndSubject_NotExists_400status() {

        when(userService.getUserByIssuerAndSubject("test", "test")).thenReturn(Optional.empty());

        client.get().uri(uriBuilder -> uriBuilder.path("/user/exists")
                .queryParam("issuer", "test")
                .queryParam("subject", "test").build())
            .exchange()
            .expectStatus().isNotFound();

        verify(userService, times(1)).getUserByIssuerAndSubject(eq("test"), eq("test"));
    }

    @Test
    void checkUserExistsByIssuerAndSubject_Exists_200Status() {
        when(userService.getUserByIssuerAndSubject("test", "test")).thenReturn(Optional.of(UserDto.builder()
            .username("test")
            .babyMonths(10)
            .content("소개글")
            .build()));

        client.get().uri(uriBuilder -> uriBuilder
                .path("/user/exists")
                .queryParam("issuer", "test")
                .queryParam("subject", "test").build())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.message").isEqualTo("success")
            .jsonPath("$.targetUsername").isEqualTo("test");

        verify(userService, times(1)).getUserByIssuerAndSubject(eq("test"), eq("test"));
    }

    @Test
    void createUser_InvalidBabyMonths_400Status() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("sub", "sub");
        fd.part("iss", "iss");
        fd.part("first", "first");
        fd.part("last", "last");
        fd.part("email", "email@emal.com");
        fd.part("emailVerified", true);
        fd.part("targetUsername", "targetUsername");
        fd.part("babyMonths", -1);
        fd.part("legalAddressCode", "123");

        client.post().uri("/user")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.babyMonths").isEqualTo(ms.getMessage("Min.CreateUserPayload.babyMonths", null, null));
    }

    @Test
    void createUser_NullFields_400Status() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("emailVerified", true);
        fd.part("babyMonths", 1);


        client.post().uri("/user")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.length()").isEqualTo(7)
            .jsonPath("$.sub").isEqualTo("공백일 수 없습니다")
            .jsonPath("$.iss").isEqualTo("공백일 수 없습니다")
            .jsonPath("$.first").isEqualTo("공백일 수 없습니다")
            .jsonPath("$.last").isEqualTo("공백일 수 없습니다")
            .jsonPath("$.email").isEqualTo("공백일 수 없습니다")
            .jsonPath("$.targetUsername").isEqualTo(ms.getMessage("NotBlank.CreateUserPayload.username", null, null))
            .jsonPath("$.legalAddressCode").isEqualTo("공백일 수 없습니다");
    }

    @Test
    void createUser_Valid_200Status() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("sub", "sub");
        fd.part("iss", "iss");
        fd.part("first", "first");
        fd.part("last", "last");
        fd.part("email", "email@email.com");
        fd.part("emailVerified", true);
        fd.part("targetUsername", "targetUsername");
        fd.part("babyMonths", 1);
        fd.part("legalAddressCode", "123");

        doNothing().when(userService).saveUser(any(UserDto.class));

        client.post().uri("/user")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.message").isEqualTo("success");

        verify(userService).saveUser(argThat(arg -> {
            assertEquals(arg.claims().subject(), "sub");
            assertEquals(arg.claims().issuer(), "iss");
            assertEquals(arg.claims().first(), "first");
            assertEquals(arg.claims().last(), "last");
            assertEquals(arg.claims().email(), "email@email.com");
            assertEquals(arg.claims().emailVerified(), true);
            assertEquals(arg.username(), "targetUsername");
            assertEquals(arg.babyMonths(), 1);
            assertEquals(arg.legalAddressCode(), "123");
            return true;
        }));
    }

    @Test
    void upadateUser_NullUsername_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();

        client.patch().uri("/user")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void upadateUser_BlankUsername_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "");

        client.patch().uri("/user")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void upadateUser_NullNextUsername_200() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "targetUsername");

        client.patch().uri("/user")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void upadateUser_BlankNextUsername_400() {
        MultipartBodyBuilder fd = new MultipartBodyBuilder();
        fd.part("targetUsername", "targetUsername");
        fd.part("nextUsername", "");

        client.patch().uri("/user")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(fromMultipartData(fd.build()))
            .exchange()
            .expectStatus().isBadRequest();
    }
}