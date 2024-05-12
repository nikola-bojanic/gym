package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainingTypeConverter;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.dto.TrainingTypeResponseDTO;
import com.nikolabojanic.model.TrainingTypeEntity;
import com.nikolabojanic.service.TrainingTypeService;
import com.nikolabojanic.service.UserService;
import io.micrometer.core.instrument.Counter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private UserService userService;
    @Mock
    private TrainingTypeConverter trainingTypeConverter;
    @Mock
    private Counter trainingTypeEndpointsHitCounter;
    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Test
    void getAllTest() {
        //given
        doNothing().when(userService).authentication(any(AuthDTO.class));
        when(trainingTypeService.getAll()).thenReturn(List.of(new TrainingTypeEntity()));
        when(trainingTypeConverter.convertModelToResponse(any(TrainingTypeEntity.class)))
                .thenReturn(new TrainingTypeResponseDTO());
        //when
        ResponseEntity<List<TrainingTypeResponseDTO>> response = trainingTypeController.getAll(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}