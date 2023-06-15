package com.test.product_order.controller;

import com.test.product_order.entity.User;
import com.test.product_order.exception.BadRequest;
import com.test.product_order.dto.LoginRequest;
import com.test.product_order.dto.SignupRequest;
import com.test.product_order.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        if (StringUtils.isEmpty(loginRequest.getUsername())){
            throw new BadRequest("Username is empty");
        }
        if (StringUtils.isEmpty(loginRequest.getPassword())){
            throw new BadRequest("Password is empty");
        }
        return userService.signIn(loginRequest);
    }

    @PostMapping("/signup")
    public User registerUser(@RequestBody SignupRequest signupRequest){
        if (StringUtils.isEmpty(signupRequest.getUsername())){
            throw new BadRequest("Username is empty");
        }
        if (StringUtils.isEmpty(signupRequest.getPassword())){
            throw new BadRequest("Password is empty");
        }
        if (signupRequest.getIsAdmin() == null){
            throw new BadRequest("isAdmin is empty");
        }
        return userService.signUp(signupRequest);
    }
}
