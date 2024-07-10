package de.htw.basketmicroservice.port.consumer;

import de.htw.basketmicroservice.config.RabbitMQConfig;
import de.htw.basketmicroservice.core.domain.model.BasketItemKey;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
public class SuccessfulOrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(SuccessfulOrderConsumer.class);
    private final IBasketService basketService;

    public SuccessfulOrderConsumer(IBasketService basketService) {
        this.basketService = basketService;
    }

    @RabbitListener(queues = RabbitMQConfig.CREATE_ORDER_QUEUE)
    public void receiveCreateOrderEvent(SuccessfulOrderMessage message) {
        basketService.removeBasket(UUID.fromString(message.getUserId()));
    }

}
