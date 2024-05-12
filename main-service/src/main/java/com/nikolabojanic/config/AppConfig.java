package com.nikolabojanic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final ObjectMapper objectMapper;

    /**
     * Creates and registers a counter named "total_transactions" in the specified {@link MeterRegistry}.
     * The counter tracks the number of total transactions.
     *
     * @param meterRegistry The {@link MeterRegistry} to register the counter.
     * @return The created {@link Counter} instance for tracking total transactions.
     */
    @Bean
    public Counter totalTransactionsCounter(MeterRegistry meterRegistry) {
        return Counter.builder("total_transactions")
            .description("Number of total transactions")
            .register(meterRegistry);
    }

    /**
     * Creates and registers a counter named "user_hit_counter" in the specified {@link MeterRegistry}.
     * The counter tracks the number of requests made to user controller.
     *
     * @param meterRegistry The {@link MeterRegistry} to register the counter.
     * @return The created {@link Counter} instance for tracking number of user requests.
     */
    @Bean
    public Counter userEndpointsHitCounter(MeterRegistry meterRegistry) {
        return Counter.builder("user_hit_counter")
            .description("number of user requests")
            .register(meterRegistry);
    }

    /**
     * Creates and registers a counter named "trainee_hit_counter" in the specified {@link MeterRegistry}.
     * The counter tracks the number of requests made to trainee controller.
     *
     * @param meterRegistry The {@link MeterRegistry} to register the counter.
     * @return The created {@link Counter} instance for tracking number of trainee requests.
     */
    @Bean
    public Counter traineeEndpointsHitCounter(MeterRegistry meterRegistry) {
        return Counter.builder("trainee_hit_counter")
            .description("number of trainee requests")
            .register(meterRegistry);
    }

    /**
     * Creates and registers a counter named "trainer_hit_counter" in the specified {@link MeterRegistry}.
     * The counter tracks the number of requests made to trainer controller.
     *
     * @param meterRegistry The {@link MeterRegistry} to register the counter.
     * @return The created {@link Counter} instance for tracking number of trainer requests.
     */

    @Bean
    public Counter trainerEndpointsHitCounter(MeterRegistry meterRegistry) {
        return Counter.builder("trainer_hit_counter")
            .description("number of requests")
            .register(meterRegistry);
    }

    /**
     * Creates and registers a counter named "training_hit_counter" in the specified {@link MeterRegistry}.
     * The counter tracks the number of requests made to training controller.
     *
     * @param meterRegistry The {@link MeterRegistry} to register the counter.
     * @return The created {@link Counter} instance for tracking number of training requests.
     */
    @Bean
    public Counter trainingEndpointsHitCounter(MeterRegistry meterRegistry) {
        return Counter.builder("training_hit_counter")
            .description("number of requests")
            .register(meterRegistry);
    }

    /**
     * Creates and registers a counter named "training_type_hit_counter" in the specified {@link MeterRegistry}.
     * The counter tracks the number of requests made to trainingType controller.
     *
     * @param meterRegistry The {@link MeterRegistry} to register the counter.
     * @return The created {@link Counter} instance for tracking number of training type requests.
     */

    @Bean
    public Counter trainingTypeEndpointsHitCounter(MeterRegistry meterRegistry) {
        return Counter.builder("training_type_hit_counter")
            .description("number of requests")
            .register(meterRegistry);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

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
}