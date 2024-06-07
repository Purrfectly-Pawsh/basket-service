package de.htw.basketmicroservice.core.domain.service.inferfaces;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.service.dto.BasketDTO;
import java.util.UUID;


public interface IBasketService {

   void addToBasket(BasketItem basketItem);

   BasketDTO getBasket(UUID userId);

   BasketDTO removeBasketItem(UUID basketId, UUID itemId);
}
