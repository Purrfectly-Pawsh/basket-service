package de.htw.basketmicroservice.port.user;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.service.dto.BasketDTO;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/v1")
public class BasketController {

    private final IBasketService basketService;

    public BasketController(IBasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/baskets/{basketId}")
    public BasketDTO getBasket(@PathVariable UUID basketId) {
        return basketService.getBasket(basketId);
    }

    @DeleteMapping("baskets/{basketId}/items/{itemId}")
    public BasketDTO removeItem(@PathVariable UUID basketId, @PathVariable UUID itemId) {
        basketService.removeBasketItem(basketId, itemId);
        return basketService.getBasket(basketId);
    }

    @PutMapping("baskets/{basketId}/items/{itemId}")
    public BasketDTO changeItemQuantity(
            @PathVariable UUID basketId, @PathVariable UUID itemId, @RequestBody BasketItem basketItem) {
        basketService.changeBasketItemQuantity(basketId, itemId, basketItem.getQuantity());
        return basketService.getBasket(basketId);
    }

    @PutMapping("baskets/{guestBasketId}")
    public BasketDTO transferGuestToUserBasket(@PathVariable UUID guestBasketId, @RequestBody Map<String, Object> user) {
        UUID uuid = UUID.fromString(user.get("userId").toString());
        basketService.transferGuestToUserBasket(guestBasketId, uuid);
        return basketService.getBasket(uuid);
    }

}
