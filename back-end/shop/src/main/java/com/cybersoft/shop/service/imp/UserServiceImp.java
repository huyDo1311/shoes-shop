package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Role;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.entity.UserRole;
import com.cybersoft.shop.repository.RoleRepository;
import com.cybersoft.shop.repository.UserRepository;
import com.cybersoft.shop.request.SignInRequest;
import com.cybersoft.shop.request.SignUpRequest;
import com.cybersoft.shop.response.user.UserResponse;
import com.cybersoft.shop.service.UserService;
import com.cybersoft.shop.component.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public User signUp(SignUpRequest signUpRequest) throws Exception {
        if (!signUpRequest.getEmail().isBlank() && userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        User newUser = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();

        UserRole userRoleLink = UserRole.builder()
                .user(newUser)
                .role(userRole)
                .build();

        newUser.setUserRoles(List.of(userRoleLink));

        return userRepository.save(newUser);
    }

    @Override
    public String signIn(SignInRequest signInRequest) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(signInRequest.getEmail());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(passwordEncoder.matches(signInRequest.getPassword(),user.getPassword())){

                String accessToken = "Bearer " + jwtTokenUtil.generateAccessToken(user);

                return accessToken;
            }
        }
        throw new RuntimeException("Invalid email or password");
    }

    @Override
    public User findUserByEmail(SignInRequest signInRequest) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(signInRequest.getEmail());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return user;
        }
        return userOptional.orElseThrow();
    }

    @Override
    public UserResponse getInfo(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));
        return UserResponse.toDTOUser(user);
    }
}
