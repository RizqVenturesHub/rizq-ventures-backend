package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.LoginReq;
import com.rizq_venture.rizqconnects.dto.UserRequest;
import com.rizq_venture.rizqconnects.dto.UserResponse;

public interface UserService {

    public UserResponse createUser(UserRequest userRequest);
    public UserResponse updateUser(Long userId,UserRequest userRequest);

    String userLogin(LoginReq loginReq);
}
