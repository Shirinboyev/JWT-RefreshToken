package uz.pdp.springboot.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.pdp.springboot.dto.ErrorBodyDTO;
import uz.pdp.springboot.exception.UsernameOrPasswordWrong;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandlers {

    @ExceptionHandler(UsernameOrPasswordWrong.class)
    public ErrorBodyDTO usernameOrPasswordWrong(HttpServletRequest req, UsernameOrPasswordWrong exception) {
        return new ErrorBodyDTO(
                exception.getStatus().value(),
                req.getRequestURI(),
                req.getRequestURL().toString(),
                exception.getClass().toString(),
                exception.getMessage(),
                LocalDateTime.now());
    }

}
