package de.htw.basketmicroservice.port.consumer;

import de.htw.basketmicroservice.config.RabbitMQConfig;
import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class ProductConsumer {

    private final IBasketService basketService;

    public ProductConsumer(IBasketService basketService) {
        this.basketService = basketService;
    }

    @RabbitListener(queues = RabbitMQConfig.ADD_PRODUCT_QUEUE)
    public void receiveAddProductEvent(ProductMessage message) {
        BasketItem basketItem = ProductMapper.toBasketItem(message);
        basketService.addToBasket(basketItem);
    }

}
