package uz.pdp.springboot.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component

public class SecurityUtils {
    public int getUser() {
        return new Random().nextInt(100);
    }
}
