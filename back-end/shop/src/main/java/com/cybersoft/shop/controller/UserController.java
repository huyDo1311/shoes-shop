package com.cybersoft.shop.controller;

import com.cybersoft.shop.entity.RefreshToken;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.request.GetUserInfoRequest;
import com.cybersoft.shop.request.RefreshTokenRequest;
import com.cybersoft.shop.request.SignInRequest;
import com.cybersoft.shop.request.SignUpRequest;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.response.user.RefreshTokenResponse;
import com.cybersoft.shop.response.user.SignInResponse;
import com.cybersoft.shop.response.user.UserResponse;
import com.cybersoft.shop.service.RefreshTokenService;
import com.cybersoft.shop.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseObject> signUp(@RequestBody SignUpRequest signUpRequest) throws Exception {
        User user = userService.signUp(signUpRequest);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.CREATED.value())
                .data(UserResponse.toDTOUser(user))
                .message("Account registration successful")
                .build());
    };

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseObject> signUp(@RequestBody SignInRequest signInRequest) throws Exception {
//            SecretKey key = Jwts.SIG.HS256.key().build();
//            String secretString = Encoders.BASE64.encode(key.getEncoded());

        String accessToken = userService.signIn(signInRequest);
        User user = userService.findUserByEmail(signInRequest);
        String refreshToken = refreshTokenService.createToken(signInRequest.getEmail());

        SignInResponse signInResponse = SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserResponse.toDTOUser(user))
                .build();

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Login successfully")
                        .data(signInResponse)
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        RefreshTokenResponse tokens = refreshTokenService.refreshToken(refreshTokenRequest.getRefreshToken());

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/info")
    public  ResponseEntity<?> getUserInfo(@Valid @RequestBody GetUserInfoRequest request){

        UserResponse userResponse = userService.getInfo(request.getEmail());
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get user successfully")
                        .data(userResponse)
                        .build()
        );
    }

}
