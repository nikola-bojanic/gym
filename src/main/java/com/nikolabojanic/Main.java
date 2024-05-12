package com.nikolabojanic;

import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.service.TraineeService;
import com.nikolabojanic.service.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Slf4j
@Configuration
@ComponentScan("com.nikolabojanic")
@PropertySource("classpath:application.properties")
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}