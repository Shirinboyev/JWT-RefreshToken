package uz.pdp.springboot.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tokens {
    private String accessToken;
    private String refreshToken;

    public Tokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    }