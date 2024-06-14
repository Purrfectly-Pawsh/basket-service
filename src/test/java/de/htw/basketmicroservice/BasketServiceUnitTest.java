package de.htw.basketmicroservice;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.model.BasketItemKey;
import de.htw.basketmicroservice.core.domain.service.dto.BasketDTO;
import de.htw.basketmicroservice.core.domain.service.impl.BasketService;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceUnitTest {

    @Mock
    IBasketRepository basketRepository;
    @InjectMocks
    BasketService basketService;

    UUID basketOne;
    BasketItem catTree;
    BasketItemKey catTreeKey;

    @BeforeEach
    void setUpItem() {
        basketOne = new UUID(1L, 1L);
        UUID catTreeId = new UUID(1L, 2L);
        catTreeKey = new BasketItemKey(basketOne, catTreeId);
        String name = "Cat Tree";
        BigDecimal price = BigDecimal.valueOf(49.99);
        String url = "http://dummy/catTreePicture";
        int catTreeQuantity = 1;

        catTree = BasketItem.builder()
                .basketItemId(catTreeId)
                .basketId(basketOne)
                .name(name)
                .unitPrice(price)
                .imageUrl(url)
                .quantity(catTreeQuantity)
                .build();
    }

    @Test
    void shouldSaveGivenItemUnchangedQuantityIfItemDoesntExistInBasket() {
        catTree.setQuantity(1);
        int expectedQuantity = 1;

        when(basketRepository.findById(catTreeKey)).thenReturn(Optional.empty());

        basketService.addToBasket(catTree);

        ArgumentCaptor<BasketItem> argument = ArgumentCaptor.forClass(BasketItem.class);
        verify(basketRepository, times(1)).save(argument.capture());
        Assertions.assertEquals(expectedQuantity, argument.getValue().getQuantity());
    }

    @Test
    void shouldSaveItemWithUpdatedQuantityIfItemAlreadyExistsInBasket() {
        catTree.setQuantity(1);
        int expectedQuantity = 2;

        when(basketRepository.findById(catTreeKey)).thenReturn(Optional.of(catTree));

        basketService.addToBasket(catTree);

        ArgumentCaptor<BasketItem> argument = ArgumentCaptor.forClass(BasketItem.class);
        verify(basketRepository, times(1)).save(argument.capture());
        Assertions.assertEquals(expectedQuantity, argument.getValue().getQuantity());
    }

    @Test
    void shouldConsiderQuantityWhenCalculatingTheTotalBasketPrice() {
        int catTreeQuantity = 4;
        catTree.setQuantity(catTreeQuantity);
        BigDecimal catTreePrice = catTree.getUnitPrice();
        BigDecimal expectedPrice = catTreePrice.multiply(BigDecimal.valueOf(catTreeQuantity));

        List<BasketItem> basketItems = new ArrayList<>();
        basketItems.add(catTree);
        when(basketRepository.getItemsByBasketId(basketOne)).thenReturn(basketItems);

        BasketDTO basket = basketService.getBasket(basketOne);

        Assertions.assertEquals(expectedPrice, basket.getTotalPrice());
    }

    @Test
    void shouldAddQuantityCorrectlyWhenItemExistsInBasket() {
        catTree.setQuantity(1);
        int newQuantity = 2;
        int expectedQuantity = 2;

        when(basketRepository.findById(catTreeKey)).thenReturn(Optional.of(catTree));

        basketService.changeBasketItemQuantity(catTree.getBasketId(), catTree.getBasketItemId(), newQuantity);

        ArgumentCaptor<BasketItem> argument = ArgumentCaptor.forClass(BasketItem.class);
        verify(basketRepository, times(1)).save(argument.capture());
        Assertions.assertEquals(expectedQuantity, argument.getValue().getQuantity());

    }

    @Test
    void shouldReduceQuantityCorrectlyWhenItemExistsInBasket() {
        catTree.setQuantity(4);
        int newQuantity = 3;
        int expectedQuantity = 3;

        when(basketRepository.findById(catTreeKey)).thenReturn(Optional.of(catTree));

        basketService.changeBasketItemQuantity(catTree.getBasketId(), catTree.getBasketItemId(), newQuantity);

        ArgumentCaptor<BasketItem> argument = ArgumentCaptor.forClass(BasketItem.class);
        verify(basketRepository, times(1)).save(argument.capture());
        Assertions.assertEquals(expectedQuantity, argument.getValue().getQuantity());

    }

    @Test
    void shouldNotCreateIfItemDoesNotExistInBasket() {
        int newQuantity = 1;

        when(basketRepository.findById(catTreeKey)).thenReturn(Optional.empty());

        basketService.changeBasketItemQuantity(catTree.getBasketId(), catTree.getBasketItemId(), newQuantity);

        verify(basketRepository, times(0)).save(any(BasketItem.class));
        verify(basketRepository, times(0)).delete(any(BasketItem.class));

    }

    @Test
    void shouldNotUpdateIfNewQuantityIsZero() {
        catTree.setQuantity(1);
        int newQuantity = 0;
        when(basketRepository.findById(catTreeKey)).thenReturn(Optional.of(catTree));

        basketService.changeBasketItemQuantity(catTree.getBasketId(), catTree.getBasketItemId(), newQuantity);

        verify(basketRepository, times(0)).save(ArgumentMatchers.any());
    }

    @Test
    void shouldNotUpdateIfNewQuantityIsNegative() {
        catTree.setQuantity(1);
        int newQuantity = -1;
        when(basketRepository.findById(catTreeKey)).thenReturn(Optional.of(catTree));

        basketService.changeBasketItemQuantity(catTree.getBasketId(), catTree.getBasketItemId(), newQuantity);

        verify(basketRepository, times(0)).save(ArgumentMatchers.any());
    }

}
