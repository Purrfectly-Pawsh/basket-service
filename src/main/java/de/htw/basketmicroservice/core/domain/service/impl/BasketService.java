package de.htw.basketmicroservice.core.domain.service.impl;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.model.BasketItemKey;
import de.htw.basketmicroservice.core.domain.service.dto.BasketDTO;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketRepository;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
public class BasketService implements IBasketService {

    private final IBasketRepository basketRepository;

    public BasketService(IBasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    public void addToBasket(BasketItem item) {
        if (isAlreadyInBasket(item)) {
            updateQuantity(item);
        } else {
            basketRepository.save(item);
        }
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

    private boolean isAlreadyInBasket(BasketItem item) {
        return basketRepository.findById(makePrimaryKey(item)).isPresent();
    }

    private void updateQuantity(BasketItem newItem) {
        BasketItem oldItem = basketRepository.findById(makePrimaryKey(newItem)).get();
        newItem.setQuantity(oldItem.getQuantity() + newItem.getQuantity());
        basketRepository.save(newItem);
    }

    private BasketItemKey makePrimaryKey(BasketItem item) {
        return new BasketItemKey(item.getBasketId(), item.getBasketItemId());
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

}
