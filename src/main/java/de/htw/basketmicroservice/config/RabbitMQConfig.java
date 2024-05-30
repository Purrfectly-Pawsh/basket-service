package de.htw.basketmicroservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;


@Configuration
public class RabbitMQConfig {

    public static final String CREATE_BASKET_QUEUE = "create_basket_queue";
    public static final String ADD_PRODUCT_QUEUE = "add_product_queue";

    public static final String BASKET_EXCHANGE = "basket_exchange";
    public static final String PRODUCT_EXCHANGE = "product_exchange";

    @Bean
    public Queue createBasketQueue() {
        return new Queue(CREATE_BASKET_QUEUE, false);
    }

    @Bean
    public Queue addProductQueue() {
        return new Queue(ADD_PRODUCT_QUEUE, false);
    }

    @Bean
    public TopicExchange basketTopicExchange() {
        return new TopicExchange(BASKET_EXCHANGE);
    }

    @Bean
    public TopicExchange productTopicExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Binding bindCreateBasketQueue() {
        return BindingBuilder.bind(createBasketQueue()).to(basketTopicExchange()).with("basket.create");
    }

    @Bean
    public Binding bindAddProductQueue() {
        return BindingBuilder.bind(addProductQueue()).to(productTopicExchange()).with("product.add");
    }

    @Bean
    public RabbitTemplate productTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

}
