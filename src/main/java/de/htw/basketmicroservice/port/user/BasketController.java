package de.htw.basketmicroservice.port.user;

import de.htw.basketmicroservice.core.domain.service.dto.BasketDTO;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
public class BasketController {

    private final IBasketService basketService;

    public BasketController(IBasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/baskets/{basketId}")
    public BasketDTO getBasket(@PathVariable UUID basketId) {
        return basketService.getBasket(basketId);
    }

}
