package de.htw.basketmicroservice.core.domain.service.impl;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.model.BasketItemKey;
import de.htw.basketmicroservice.core.domain.service.dto.BasketDTO;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketRepository;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class BasketService implements IBasketService {

    private static final int REMOVE_ONE = -1;
    private final IBasketRepository basketRepository;

    public BasketService(IBasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    public BasketDTO getBasket(UUID basketId) {
        List<BasketItem> items = basketRepository.getItemsByBasketId(basketId);
        return BasketDTO.builder()
                .basketId(basketId.toString())
                .totalPrice(calculateTotalPrice(items))
                .basketItems(items)
                .build();
    }

    @Override
    public void addToBasket(BasketItem item) {
        BasketItemKey itemKey = item.getKey();
        if (itemExistsInBasket(itemKey)) {
            updateQuantity(itemKey, item.getQuantity());
        } else {
            basketRepository.save(item);
        }
    }

    @Override
    public BasketDTO removeBasketItem(UUID basketId, UUID itemId) {
        BasketItemKey key = new BasketItemKey(basketId, itemId);
        if (itemExistsInBasket(key)) {
            updateQuantity(key, REMOVE_ONE);
        }
        return getBasket(basketId);
    }

    private void updateQuantity(BasketItemKey key, int additional) throws NoSuchElementException {
        BasketItem itemToUpdate = basketRepository.findById(key).get();
        int newQuantity = itemToUpdate.getQuantity() + additional;
        if (newQuantity > 0) {
            itemToUpdate.setQuantity(newQuantity);
            basketRepository.save(itemToUpdate);
        } else {
            basketRepository.delete(itemToUpdate);
        }
    }

    private BigDecimal calculateTotalPrice(List<BasketItem> items) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (BasketItem item : items) {
            BigDecimal quantity = new BigDecimal(item.getQuantity());
            BigDecimal unitPrice = item.getUnitPrice();
            totalPrice = totalPrice.add(quantity.multiply(unitPrice));
        }
        return totalPrice;
    }

    private boolean itemExistsInBasket(BasketItemKey key) {
        return basketRepository.findById(key).isPresent();
    }

}
