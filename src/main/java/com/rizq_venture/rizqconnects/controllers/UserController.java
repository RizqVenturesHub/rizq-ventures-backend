package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.LoginReq;
import com.rizq_venture.rizqconnects.dto.UserRequest;
import com.rizq_venture.rizqconnects.dto.UserResponse;
import com.rizq_venture.rizqconnects.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);

    }
    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@Valid @RequestBody LoginReq loginReq){
        return new ResponseEntity<>(userService.userLogin(loginReq),HttpStatus.OK);
    }

}
