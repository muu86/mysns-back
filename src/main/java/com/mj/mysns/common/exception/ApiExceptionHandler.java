package com.mj.mysns.common.exception;

import com.mj.mysns.location.exception.AddressNotFoundException;
import com.mj.mysns.user.exception.UserDuplicatedException;
import com.mj.mysns.user.exception.UsernameDuplicatedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        Map<String, String> map = new HashMap<>();
        if (bindingResult.hasFieldErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(e -> {
                map.put(e.getField(), e.getDefaultMessage());
            });
        }
        if (bindingResult.hasGlobalErrors()) {
            List<ObjectError> globalErrors = bindingResult.getGlobalErrors();
            globalErrors.forEach(e -> {
                map.put(e.getObjectName(), e.getDefaultMessage());
            });
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(map);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAddressNotFoundException(
        AddressNotFoundException exception) {

        Map<String, String> map = new HashMap<>();
        map.put("address", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(map);
    }

    @ExceptionHandler(UsernameDuplicatedException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatedUsernameException(
        UsernameDuplicatedException exception) {

        Map<String, String> map = new HashMap<>();
        map.put("targetUsername", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(map);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatedUserExeption(
        UserDuplicatedException exception) {

        Map<String, String> map = new HashMap<>();
        map.put("username", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(map);
    }
}
