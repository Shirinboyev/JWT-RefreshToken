package uz.pdp.springboot.service;

import org.springframework.stereotype.Service;
import uz.pdp.springboot.model.RefreshToken;
import uz.pdp.springboot.repository.RefreshTokenRepository;
import uz.pdp.springboot.repository.UserRepository;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository authUserRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, CustomUserDetailsService customUserDetailsService, UserRepository authUserRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.authUserRepository = authUserRepository;
    }

    public void save(String refreshToken, String username) {
        refreshTokenRepository.save(
                RefreshToken.builder().
                        userId(getIdWithUsername(username)).
                        token(refreshToken).
                        build()
        );
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new RuntimeException("Could not find Token"));
    }

    public Long getIdWithUsername(String username) {
        return authUserRepository.getIdWithUsername(username);
    }
}