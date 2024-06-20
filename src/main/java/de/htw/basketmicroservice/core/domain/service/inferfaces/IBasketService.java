package de.htw.basketmicroservice.core.domain.service.inferfaces;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.service.dto.BasketDTO;
import java.util.UUID;


public interface IBasketService {

   void addToBasket(BasketItem basketItem);

   BasketDTO getBasket(UUID userId);

   void removeBasketItem(UUID basketId, UUID itemId);

   void changeBasketItemQuantity(UUID basketId, UUID itemId, int quantity);

   void transgerGuestToUserBasket(UUID guestBasketId, UUID userId);
}
