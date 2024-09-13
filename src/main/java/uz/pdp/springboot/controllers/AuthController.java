package uz.pdp.springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springboot.dto.AuthenticationDto;
import uz.pdp.springboot.model.*;
import uz.pdp.springboot.repository.UserRepository;
import uz.pdp.springboot.service.JwtService;
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

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String accessToken = jwtService.generateAccessToken(loginRequest.getUsername());
        String refreshToken = jwtService.generateRefreshToken(loginRequest.getUsername());
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestParam String username) {
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);
        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (jwtService.validateRefreshToken(refreshToken, "refreshSecretKey")) {
            String username = jwtService.getUsernameFromToken(refreshToken, "refreshSecretKey");
            String newAccessToken = jwtService.generateAccessToken(username);
            return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token yaroqsiz!");
    }
    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationDto> authentication(@RequestParam("username")String username, @RequestParam("password")String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        String accessToken = jwtUtil.generateAccessToken(username);
        return ResponseEntity.ok(new AuthenticationDto(accessToken));
    }


}
