package de.htw.basketmicroservice.core.domain.service.inferfaces;

import de.htw.basketmicroservice.core.domain.model.Basket;
import de.htw.basketmicroservice.core.domain.model.BasketItem;

public interface IBasketService {

   void createBasket(Long userId);

   void addBasketItemToBasket(BasketItem basketItem, Long userId);

   Basket getBasket(Long userId);


}
