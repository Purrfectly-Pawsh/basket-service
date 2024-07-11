package de.htw.basketmicroservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;


@Configuration
public class RabbitMQConfig {

    public static final String ADD_PRODUCT_QUEUE = "add_product_queue";
    public static final String PRODUCT_EXCHANGE = "product_exchange";

    public static final String DELETE_BASKET_QUEUE = "delete_basket_queue";
    public static final String ORDER_EXCHANGE = "order_exchange";

    @Bean
    public Queue addProductQueue() {
        return new Queue(ADD_PRODUCT_QUEUE, false);
    }

    @Bean
    public Queue deleteBasketQueue() {
        return new Queue(DELETE_BASKET_QUEUE, false);
    }

    @Bean
    public TopicExchange productTopicExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    @Bean FanoutExchange orderExchange() {
        return new FanoutExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding bindAddProductQueue() {
        return BindingBuilder.bind(addProductQueue()).to(productTopicExchange()).with("product.add");
    }

    @Bean Binding bindCreateOrderQueue() {
        return BindingBuilder.bind(deleteBasketQueue()).to(orderExchange());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate productTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

}
