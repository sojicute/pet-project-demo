package com.sojicute.petprojectdemo.api;

import com.sojicute.petprojectdemo.domain.User;
import com.sojicute.petprojectdemo.domain.dto.AuthRequest;
import com.sojicute.petprojectdemo.domain.dto.RegisterRequest;
import com.sojicute.petprojectdemo.repository.UserRepository;
import com.sojicute.petprojectdemo.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()
                            )
                    );

            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

            // Generate JWT token
            String token = jwtUtil.generateAccessToken(request.getUsername());

            return ResponseEntity.ok()
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        token
                    )
                    .body(userDetails);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        String token = jwtUtil.generateAccessToken(user.getUsername());

        userRepository.save(user);
        return ResponseEntity.ok().body(token);
    }
}
