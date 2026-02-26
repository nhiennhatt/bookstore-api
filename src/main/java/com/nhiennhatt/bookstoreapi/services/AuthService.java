package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.common.enums.UserRole;
import com.nhiennhatt.bookstoreapi.common.enums.UserStatus;
import com.nhiennhatt.bookstoreapi.dto.user.CreateUserResponse;
import com.nhiennhatt.bookstoreapi.models.CustomUserDetails;
import com.nhiennhatt.bookstoreapi.models.User;
import com.nhiennhatt.bookstoreapi.repository.UserRepository;
import com.nhiennhatt.bookstoreapi.utils.JwtUtil;
import com.nhiennhatt.bookstoreapi.validations.user.CreateUserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil accessTokenService;

    public CreateUserResponse createUser(CreateUserValidation user) {
        User userEntity = new User();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(hashedPassword);
        userEntity.setVerified(false);
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setRole(UserRole.ROLE_CUSTOMER);
        userRepository.save(userEntity);

        CreateUserResponse res = CreateUserResponse
                .builder()
                .id(userEntity.getId())
                .build();
        res.setId(userEntity.getId());
        return res;
    }

    public String loginUser(String username, String password) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        try {
            return accessTokenService.generateToken(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }
}
