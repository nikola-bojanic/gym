package com.nikolabojanic;

import com.nikolabojanic.model.User;
import com.nikolabojanic.service.TraineeService;
import com.nikolabojanic.service.TrainerService;
import com.nikolabojanic.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;


@Slf4j
@PropertySource("classpath:application.properties")
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(Main.class);
        ctx.scan("com.nikolabojanic");
        ctx.refresh();
        log.info("Generating trainers' usernames and passwords");
        TrainerService trainerService = ctx.getBean(TrainerService.class);
        UserService userService = ctx.getBean(UserService.class);
        for (User user : userService.profile(trainerService.getTrainerUsers())) {
            log.info("User with username and password: {} {}", user.getUsername(), user.getPassword());
        }
        log.info("----------------------");
        log.info("Generating trainees' usernames and passwords.");
        TraineeService traineeService = ctx.getBean(TraineeService.class);
        for (User user : userService.profile(traineeService.getTraineeUsers())) {
            log.info("User with username and password: {} {}", user.getUsername(), user.getPassword());
        }
    }
}