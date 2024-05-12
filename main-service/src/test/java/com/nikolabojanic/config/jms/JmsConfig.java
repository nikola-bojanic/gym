package com.nikolabojanic.config.jms;

import jakarta.jms.ConnectionFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;

@Slf4j
@RequiredArgsConstructor
@TestConfiguration
@EnableJms
public class JmsConfig {
    private final ConnectionFactory connectionFactory;
    private final MessageConverter jsonMessageConverter;

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
        factory.setMessageConverter(jsonMessageConverter);
        factory.setErrorHandler(t -> log.error(t.getMessage()));
        return factory;
    }

    @Bean
    public BlockingQueue<Object> blockingQueue() {
        return new LinkedBlockingQueue<>();
    }
}
