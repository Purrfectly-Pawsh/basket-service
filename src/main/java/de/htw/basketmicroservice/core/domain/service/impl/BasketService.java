package de.htw.basketmicroservice.core.domain.service.impl;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.model.BasketItemKey;
import de.htw.basketmicroservice.core.domain.service.dto.BasketDTO;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketRepository;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class BasketService implements IBasketService {

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
        if (itemExistsInBasket(item.getKey())) {
            int newQuantity = calculateNewQuantity(item);
            updateQuantityTo(newQuantity, item.getKey());
        } else {
            basketRepository.save(item);
        }
    }

    @Override
    public void removeBasketItem(UUID basketId, UUID itemId) {
        BasketItemKey itemKey = new BasketItemKey(basketId, itemId);
        basketRepository.deleteById(itemKey);
    }

    @Override
    public void changeBasketItemQuantity(UUID basketId, UUID itemId, int quantity) {
        BasketItemKey itemKey = new BasketItemKey(basketId, itemId);
        if (itemExistsInBasket(itemKey)) {
            updateQuantityTo(quantity, itemKey);
        }
    }

    @Override
    @Transactional
    public void transferGuestToUserBasket(UUID guestBasketId, UUID userId) {
        List<BasketItem> guestBasketItems = basketRepository.getItemsByBasketId(guestBasketId);
        basketRepository.deleteItemsByBasketId(userId);
        basketRepository.deleteItemsByBasketId(guestBasketId);
        for (BasketItem guestBasketItem : guestBasketItems) {
            BasketItem userItem = BasketItem.builder()
                    .basketItemId(guestBasketItem.getBasketItemId())
                    .basketId(userId)
                    .name(guestBasketItem.getName())
                    .unitPrice(guestBasketItem.getUnitPrice())
                    .imageUrl(guestBasketItem.getImageUrl())
                    .quantity(guestBasketItem.getQuantity())
                    .build();
            basketRepository.save(userItem);
        }
    }

    @Override
    public void deleteBasket(UUID basketId) {
        basketRepository.deleteItemsByBasketId(basketId);
    }

    private int calculateNewQuantity(BasketItem newItem) {
        BasketItem existingItem = basketRepository.findById(newItem.getKey()).get();
        return existingItem.getQuantity() + newItem.getQuantity();
    }

    private void updateQuantityTo(int quantity, BasketItemKey key) throws NoSuchElementException {
        BasketItem itemToUpdate = basketRepository.findById(key).get();
        if (quantity > 0) {
            itemToUpdate.setQuantity(quantity);
            basketRepository.save(itemToUpdate);
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
