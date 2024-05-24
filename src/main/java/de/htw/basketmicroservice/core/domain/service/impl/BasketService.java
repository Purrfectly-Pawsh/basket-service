package de.htw.basketmicroservice.core.domain.service.impl;

import de.htw.basketmicroservice.core.domain.model.Basket;
import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.service.impl.exception.BasketNotFoundException;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketRepository;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import org.springframework.stereotype.Service;

@Service
public class BasketService implements IBasketService {

    private final IBasketRepository basketRepository;

    public BasketService(IBasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    public void createBasket(Long userId) {
        basketRepository.save(new Basket(userId));
    }

    @Override
    public void addBasketItemToBasket(BasketItem basketItem, Long userId) {
        Basket basket = basketRepository.findById(userId).orElseThrow(() -> new BasketNotFoundException(userId));
        basket.addBasketItem(basketItem);
        basketRepository.save(basket);
    }

    @Override
    public Basket getBasket(Long userId) {
        return basketRepository.findById(userId).orElseThrow(() -> new BasketNotFoundException(userId));
    }

}
