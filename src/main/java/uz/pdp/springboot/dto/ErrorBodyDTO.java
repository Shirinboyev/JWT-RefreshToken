package uz.pdp.springboot.dto;

import java.time.LocalDateTime;

public record ErrorBodyDTO(
        Integer status,
        String path,
        String reason,
        String message,
        String exceptionMessage,
        LocalDateTime timestamp
) {

}
