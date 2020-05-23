package ru.bmstu.cp.rsoi.statistic.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bmstu.cp.rsoi.statistic.service.OperationListener;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("operationExchange");
    }

    @Bean
    public Queue queue() {
        return new Queue("operation-queue");
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange eventExchange) {
        return BindingBuilder
                .bind(queue)
                .to(eventExchange)
                .with("operation");
    }

    @Bean
    public OperationListener eventReceiver() {
        return new OperationListener();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
 
}