package com.nikolabojanic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJms
@Slf4j
@RequiredArgsConstructor
public class JmsConfig {
    private final ConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;


    /**
     * Registers and configures a JSON converter bean used by active mq broker.
     *
     * @return Configured JSON message converter.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        objectMapper.registerModule(new JavaTimeModule());
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    /**
     * Creates a {@link DefaultJmsListenerContainerFactory} bean for configuring JMS message listeners.
     * The factory sets up essential configurations such as connection factory, message converter, transaction manager, and error handler.
     *
     * @return A configured {@link DefaultJmsListenerContainerFactory} bean.
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setTransactionManager(jmsTransactionManager());
        factory.setErrorHandler(t -> log.error(t.getMessage()));
        return factory;
    }

    @Bean
    public PlatformTransactionManager jmsTransactionManager() {
        return new JmsTransactionManager(connectionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    /**
     * Creates a {@link JmsTemplate} bean for sending and receiving JMS messages within a Spring application.
     *
     * @return A configured {@link JmsTemplate} bean.
     */

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(jsonMessageConverter());
        jmsTemplate.setDeliveryPersistent(true);
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }

}