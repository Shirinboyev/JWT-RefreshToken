package uz.pdp.springboot.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springboot.dto.RefreshTokenDto;
import uz.pdp.springboot.model.RefreshTokenRequest;
import uz.pdp.springboot.model.Tokens;
import uz.pdp.springboot.model.User;
import uz.pdp.springboot.repository.UserRepository;
import uz.pdp.springboot.service.CustomUserDetailsService;
import uz.pdp.springboot.service.RefreshTokenService;
import uz.pdp.springboot.utils.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserRepository authUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService, RefreshTokenService refreshTokenService, RefreshTokenService refreshTokenService1) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.refreshTokenService = refreshTokenService1;
    }

    @PostMapping("/token")
    public Tokens getToken(@RequestBody RefreshTokenDto tokenRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tokenRequestDTO.username(), tokenRequestDTO.password());
        authenticationManager.authenticate(authenticationToken);
        String refreshToken = jwtTokenUtil.generateRefreshToken(tokenRequestDTO.username());
        String accessToken = jwtTokenUtil.generateRefreshToken(tokenRequestDTO.username());
        refreshTokenService.save(refreshToken, tokenRequestDTO.username());
        return new Tokens(accessToken, refreshToken);
    }

    @PostMapping("/register")
    public RefreshTokenDto register(@RequestBody RefreshTokenDto user) {
        authUserRepository.save(User.builder()
                .username(user.password())
                .password(passwordEncoder.encode(user.password()))
                .role("USER")
                .build());
        return user;
    }

    @PostMapping("/refresh")
    public Tokens refresh(@RequestBody Tokens tokens) {
        String username = jwtTokenUtil.extractUsername(tokens.getRefreshToken());
        jwtTokenUtil.validateToken(tokens.getRefreshToken(),
                customUserDetailsService.loadUserByUsername(username));
        refreshTokenService.findByToken(tokens.getRefreshToken());
        return new Tokens(jwtTokenUtil.generateToken(username), null);
    }
}