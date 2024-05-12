package com.nikolabojanic.config;

import com.nikolabojanic.logging.MDCFilter;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public Counter totalTransactionsCounter(MeterRegistry meterRegistry) {
        return Counter.builder("total_transactions")
                .description("Number of total transactions")
                .register(meterRegistry);
    }

    @Bean
    public Counter userEndpointsHitCounter(MeterRegistry meterRegistry) {
        return Counter.builder("user_hit_counter")
                .description("number of user requests")
                .register(meterRegistry);
    }

    @Bean
    public Counter traineeEndpointsHitCounter(MeterRegistry meterRegistry) {
        return Counter.builder("trainee_hit_counter")
                .description("number of trainee requests")
                .register(meterRegistry);
    }

    @Bean
    public Counter trainerEndpointsHitCounter(MeterRegistry meterRegistry) {
        return Counter.builder("trainer_hit_counter")
                .description("number of requests")
                .register(meterRegistry);
    }

    @Bean
    public Counter trainingEndpointsHitCounter(MeterRegistry meterRegistry) {
        return Counter.builder("training_hit_counter")
                .description("number of requests")
                .register(meterRegistry);
    }

    @Bean
    public Counter trainingTypeEndpointsHitCounter(MeterRegistry meterRegistry) {
        return Counter.builder("training_type_hit_counter")
                .description("number of requests")
                .register(meterRegistry);
    }

    @Bean
    public FilterRegistrationBean<MDCFilter> mdcFilter() {
        FilterRegistrationBean<MDCFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MDCFilter());
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
