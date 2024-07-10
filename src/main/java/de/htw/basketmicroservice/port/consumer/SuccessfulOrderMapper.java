package de.htw.basketmicroservice.port.consumer;

import de.htw.basketmicroservice.core.domain.model.BasketItemKey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SuccessfulOrderMapper {

    public static List<BasketItemKey> toBasketItemKeys(SuccessfulOrderMessage message) {
        List<OrderItem> orderItems = message.getItems();
        List<BasketItemKey> basketItemKeys = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            basketItemKeys.add(
                    new BasketItemKey(
                            UUID.fromString(message.getUserId()),
                            UUID.fromString(orderItem.getProductId())
                    )
            );
        }
        return basketItemKeys;
    }

}
