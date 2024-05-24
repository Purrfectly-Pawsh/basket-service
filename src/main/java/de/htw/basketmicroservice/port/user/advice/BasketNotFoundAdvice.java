package de.htw.basketmicroservice.port.user.advice;

import de.htw.basketmicroservice.core.domain.service.impl.exception.BasketNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BasketNotFoundAdvice {
    @ExceptionHandler(BasketNotFoundException.class)
    @ResponseStatus
    String basketNotFoundHandler(BasketNotFoundException ex) {
        return ex.getMessage(); }
}
