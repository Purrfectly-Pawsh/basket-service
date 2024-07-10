package de.htw.basketmicroservice.port.consumer;

import de.htw.basketmicroservice.config.RabbitMQConfig;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SuccessfulOrderConsumer {

    private final IBasketService basketService;

    public SuccessfulOrderConsumer(IBasketService basketService) {
        this.basketService = basketService;
    }

    @RabbitListener(queues = RabbitMQConfig.CREATE_ORDER_QUEUE)
    public void receiveCreateOrderEvent(SuccessfulOrderMessage message) {
        basketService.deleteBasket(UUID.fromString(message.getUserId()));
    }

}