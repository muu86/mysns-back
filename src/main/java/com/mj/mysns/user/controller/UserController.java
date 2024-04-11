package com.mj.mysns.user.controller;

import com.mj.mysns.user.dto.UpdateUser;
import com.mj.mysns.user.dto.UserDto;
import com.mj.mysns.user.dto.UserMapper;
import com.mj.mysns.user.dto.UserProfile;
import com.mj.mysns.user.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Authentication user(Authentication authentication) {
        log.info("{}", authentication);
        return authentication;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getUserProfile(@RequestParam(value = "username") String username) {
        if (username.isEmpty()) return ResponseEntity.badRequest().build();

        Optional<UserProfile> found = userService.getUserProfileByUsername(
            UserDto.builder().username(username).build());

        return found
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/exists")
    public ResponseEntity<Map<String, String>> checkUserExistsByIssuerAndSubject(
        @RequestParam("issuer") String issuer,
        @RequestParam("subject") String subject) {

        if (issuer.isEmpty() || subject.isEmpty()) return ResponseEntity.badRequest().build();

        Optional<UserDto> byUsername = userService.getUserByIssuerAndSubject(issuer, subject);

        return byUsername.map(userDto -> Map.of("message", "success", "username", byUsername.get().username()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createUser(@Valid CreateUserPayload payload) {
        UserDto userDto = UserDto.builder()
            .claims(userMapper.createUserPayloadToClaimsDto(payload))
            .username(payload.username())
            .babyMonths(payload.babyMonths())
            .legalAddressCode(payload.legalAddressCode())
            .build();

        userService.saveUser(userDto);

        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updateUser(@Valid @RequestBody UpdateUser payload) {

        // targetUsername 체크
        if (payload.username() == null || payload.username().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // nextUsername
        if (payload.nextUsername() != null && payload.nextUsername().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // addresses
        if (payload.addresses() != null && !payload.addresses().isEmpty()) {
            // 공백이 포함되거나 숫자 이외의 문자가 포함된 경우 400 status
            boolean blank = payload.addresses().stream().anyMatch(code -> {
                return code.isBlank() || code.matches(".*[^0-9].*");
            });
            if (blank) return ResponseEntity.badRequest().build();
        }

        userService.updateUser(payload);
        return null;
    }
}