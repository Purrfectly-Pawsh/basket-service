package de.htw.basketmicroservice.port.user;


import de.htw.basketmicroservice.core.domain.model.Basket;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
public class BasketController {

    private final IBasketService basketService;

    public BasketController(IBasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/baskets/{userId}")
    public Basket getBasket(@PathVariable Long userId) {
        return basketService.getBasket(userId);}

}
