package com.cybersoft.shop.controller;

import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.request.SignInRequest;
import com.cybersoft.shop.request.SignUpRequest;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.response.user.SignInResponse;
import com.cybersoft.shop.response.user.UserResponse;
import com.cybersoft.shop.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseObject> signUp(@RequestBody SignUpRequest signUpRequest) throws Exception {
        User user = userService.signUp(signUpRequest);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.CREATED.value())
                .data(UserResponse.fromUser(user))
                .message("Account registration successful")
                .build());
    };

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseObject> signUp(@RequestBody SignInRequest signInRequest) throws Exception {
//            SecretKey key = Jwts.SIG.HS256.key().build();
//            String secretString = Encoders.BASE64.encode(key.getEncoded());

        String token = userService.signIn(signInRequest);
        User user = userService.findUserByEmail(signInRequest);

        SignInResponse signInResponse = SignInResponse.builder()
                .token(token)
                .user(UserResponse.fromUser(user))
                .build();

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Login successfully")
                        .data(signInResponse)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}
