package de.htw.basketmicroservice.port.consumer;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import java.util.UUID;


public class ProductMapper {

    public static BasketItem toBasketItem(ProductMessage message) {
        return BasketItem.builder()
                .basketItemId(UUID.fromString(message.getProductId()))
                .basketId(UUID.fromString(message.getUserId()))
                .name(message.getName())
                .unitPrice(message.getUnitPrice())
                .imageUrl(message.getImageLink())
                .quantity(message.getQuantity())
                .build();
    }

}
