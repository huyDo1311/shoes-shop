package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.config.LockoutConfig;
import com.cybersoft.shop.entity.Role;
import com.cybersoft.shop.entity.User;
import com.cybersoft.shop.entity.UserRole;
import com.cybersoft.shop.messaging.SignUpEventPublisher;
import com.cybersoft.shop.messaging.event.SignUpSuccessEvent;
import com.cybersoft.shop.repository.RoleRepository;
import com.cybersoft.shop.repository.UserRepository;
import com.cybersoft.shop.request.SignInRequest;
import com.cybersoft.shop.request.SignUpRequest;
import com.cybersoft.shop.request.UserUpdateRequest;
import com.cybersoft.shop.request.VerifyEmailRequest;
import com.cybersoft.shop.response.user.UserResponse;
import com.cybersoft.shop.service.FileService;
import com.cybersoft.shop.service.LockoutService;
import com.cybersoft.shop.service.UserService;
import com.cybersoft.shop.component.JwtTokenUtil;
import com.cybersoft.shop.service.VerificationService;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private SignUpEventPublisher signUpEventPublisher;
    @Autowired
    private LockoutService lockoutService;
    @Autowired
    private VerificationService verificationService;

//    @Override
//    public User signUp(SignUpRequest signUpRequest) throws Exception {
//        if (!signUpRequest.getEmail().isBlank() && userRepository.existsByEmail(signUpRequest.getEmail())) {
//            throw new DataIntegrityViolationException("Email already exists");
//        }
//        Role userRole = roleRepository.findByRoleName("USER")
//                .orElseThrow(() -> new RuntimeException("Role USER not found"));
//
//        User newUser = User.builder()
//                .email(signUpRequest.getEmail())
//                .password(passwordEncoder.encode(signUpRequest.getPassword()))
//                .build();
//
//        UserRole userRoleLink = UserRole.builder()
//                .user(newUser)
//                .role(userRole)
//                .build();
//
//        newUser.setUserRoles(List.of(userRoleLink));
//
//        return userRepository.save(newUser);
//    }

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

        User saved = userRepository.save(newUser);

        String code = verificationService.generateAndStoreCode(saved.getEmail());

        SignUpSuccessEvent event = SignUpSuccessEvent.builder()
                .userId(saved.getId())
                .email(saved.getEmail())
                .userName(saved.getUserName())
                .code(code)
                .build();
        signUpEventPublisher.publish(event);

        return saved;
    }

    @Override
    public void verifyEmail(VerifyEmailRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        String code = request.getCode().trim();

        boolean ok = verificationService.verifyCode(email, code);
        if (!ok) {
            throw new IllegalArgumentException("Mã xác thực không đúng hoặc đã hết hạn");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (Boolean.TRUE.equals(user.getIsActive())) {
            return;
        }

        user.setIsActive(true);
        userRepository.save(user);
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @Override
    public String signIn(SignInRequest signInRequest, HttpServletRequest request) {

        String ip = getClientIp(request);
        String email = signInRequest.getEmail().trim().toLowerCase();

        String lockMsg = lockoutService.lockMessageOrNull(ip);
        if (lockMsg != null) {
            throw new RuntimeException(lockMsg);
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new RuntimeException(lockoutService.failMessage(ip));
        }

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new RuntimeException("Account has been disabled.");
        }

        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException(lockoutService.failMessage(ip));
        }

        lockoutService.onLoginSuccess(ip);

        if (verificationService.haveActiveSession(email)) {
            throw new RuntimeException("Account is already logged in from another device.");
        }

        verificationService.createOrUpdateSession(email);

        return "Bearer " + jwtTokenUtil.generateAccessToken(user);
    }


    @Override
    public void signOut(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            throw new RuntimeException("Authorization token is required");
        }

        String token = authHeader;
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = jwtTokenUtil.getClaimsFromToken(token);
        String email = claims.get("email", String.class);

        if (email == null || email.isBlank()) {
            throw new RuntimeException("Invalid token");
        }

        verificationService.clearSession(email);
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

    @Override
    public UserResponse updateUser(UserUpdateRequest request, MultipartFile avatar) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(request.getUserName() != null)
            user.setUserName(request.getUserName().trim());
        if(request.getDateOfBirth() != null)
            user.setDateOfBirth(request.getDateOfBirth().trim());
        if(request.getAddress() != null)
            user.setAddress(request.getAddress().trim());
        if(request.getPhone() != null)
            user.setPhone(request.getPhone().trim());
        if (request.getAvatar() != null)
            user.setAvatar(request.getAvatar().trim());
        userRepository.save(user);
        return UserResponse.toDTOUser(user);
    }
}
