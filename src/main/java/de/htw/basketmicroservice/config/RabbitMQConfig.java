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

    public static final String ADD_PRODUCT_QUEUE = "add_product_queue";
    public static final String PRODUCT_EXCHANGE = "product_exchange";

    public static final String CREATE_ORDER_QUEUE = "create_order_queue";
    public static final String ORDER_EXCHANGE = "order_exchange";

    @Bean
    public Queue addProductQueue() {
        return new Queue(ADD_PRODUCT_QUEUE, false);
    }

    @Bean
    public Queue createOrderQueue() {
        return new Queue(CREATE_ORDER_QUEUE, false);
    }

    @Bean
    public TopicExchange productTopicExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    @Bean TopicExchange orderTopicExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding bindAddProductQueue() {
        return BindingBuilder.bind(addProductQueue()).to(productTopicExchange()).with("product.add");
    }

    @Bean Binding bindCreateOrderQueue() {
        return BindingBuilder.bind(createOrderQueue()).to(orderTopicExchange()).with("order.create");
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
