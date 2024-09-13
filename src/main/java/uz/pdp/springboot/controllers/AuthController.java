package uz.pdp.springboot.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springboot.dto.AuthenticationDto;
import uz.pdp.springboot.model.TokenResponse;
import uz.pdp.springboot.model.User;
import uz.pdp.springboot.repository.UserRepository;
import uz.pdp.springboot.utils.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestParam String username) {
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);
        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String refreshToken) {
        if (jwtUtil.validateToken(refreshToken, jwtUtil.extractUsername(refreshToken))) {
            String newAccessToken = jwtUtil.generateAccessToken(jwtUtil.extractUsername(refreshToken));
            return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
        } else {
            return ResponseEntity.status(403).body("Invalid refresh token");
        }
    }
    @PostMapping("/register")
    public User register(@RequestBody User user){
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        User save = userRepository.save(user);
        return user;
    }
    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationDto> authentication(@RequestParam("username")String username, @RequestParam("password")String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        String accessToken = jwtUtil.generateAccessToken(username);
        return ResponseEntity.ok(new AuthenticationDto(accessToken));
    }



}
