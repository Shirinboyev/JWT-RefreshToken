package uz.pdp.springboot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class UsernameOrPasswordWrong extends RuntimeException{
    private HttpStatus status;
    public UsernameOrPasswordWrong(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}

