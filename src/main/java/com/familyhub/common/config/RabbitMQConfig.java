package com.familyhub.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "familyhub.exchange";

    // Queues
    public static final String NOTIFICATION_QUEUE = "familyhub.notification";
    public static final String INVENTORY_QUEUE = "familyhub.inventory";
    public static final String REPORT_QUEUE = "familyhub.report";

    // DLQ
    public static final String DLX_EXCHANGE = "familyhub.dlx.exchange";
    public static final String NOTIFICATION_DLQ = "familyhub.notification.dlq";
    public static final String INVENTORY_DLQ = "familyhub.inventory.dlq";

    // Routing keys
    public static final String NOTIFICATION_KEY = "notification.*";
    public static final String INVENTORY_KEY = "inventory.*";
    public static final String REPORT_KEY = "report.*";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(DLX_EXCHANGE);
    }

    // Notification queue with DLQ
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", NOTIFICATION_DLQ)
                .build();
    }

    @Bean
    public Queue notificationDlq() {
        return QueueBuilder.durable(NOTIFICATION_DLQ).build();
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue()).to(exchange()).with(NOTIFICATION_KEY);
    }

    @Bean
    public Binding notificationDlqBinding() {
        return BindingBuilder.bind(notificationDlq()).to(dlxExchange()).with(NOTIFICATION_DLQ);
    }

    // Inventory queue
    @Bean
    public Queue inventoryQueue() {
        return QueueBuilder.durable(INVENTORY_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", INVENTORY_DLQ)
                .build();
    }

    @Bean
    public Queue inventoryDlq() {
        return QueueBuilder.durable(INVENTORY_DLQ).build();
    }

    @Bean
    public Binding inventoryBinding() {
        return BindingBuilder.bind(inventoryQueue()).to(exchange()).with(INVENTORY_KEY);
    }

    // Report queue
    @Bean
    public Queue reportQueue() {
        return QueueBuilder.durable(REPORT_QUEUE).build();
    }

    @Bean
    public Binding reportBinding() {
        return BindingBuilder.bind(reportQueue()).to(exchange()).with(REPORT_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
