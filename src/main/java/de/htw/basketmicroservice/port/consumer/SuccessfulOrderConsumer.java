package de.htw.basketmicroservice.port.consumer;

import de.htw.basketmicroservice.config.RabbitMQConfig;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@Slf4j
public class SuccessfulOrderConsumer {

    private final IBasketService basketService;

    public SuccessfulOrderConsumer(IBasketService basketService) {
        this.basketService = basketService;
    }

    @RabbitListener(queues = RabbitMQConfig.DELETE_BASKET_QUEUE)
    public void receiveDeleteBasketEvent(SuccessfulOrderMessage message) {
        log.info(message.toString());
        basketService.removeBasket(UUID.fromString(message.getUserId()));
    }

}
