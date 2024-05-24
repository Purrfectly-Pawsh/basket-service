package de.htw.basketmicroservice.core.domain.service.impl.exception;

public class BasketNotFoundException extends RuntimeException {
    public BasketNotFoundException(Long userId) { super("Basket " + userId + " not found");
    }
}
