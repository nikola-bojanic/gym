package com.nikolabojanic.service;

import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.repository.TrainingTypeRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    void getAllTest() {
        //arrange
        when(trainingTypeRepository.findAll()).thenReturn(new ArrayList<>());
        //act
        List<TrainingTypeEntity> types = trainingTypeService.getAll();
        //assert
        assertThat(types).isNotNull();
    }

    @Test
    void findByIdTest() {
        //arrange
        when(trainingTypeRepository.findById(anyLong())).thenReturn(Optional.of(new TrainingTypeEntity()));
        //act
        TrainingTypeEntity type = trainingTypeService.findById(
                Long.parseLong(RandomStringUtils.randomNumeric(5)));
        //assert
        assertThat(type).isNotNull();
    }

    @Test
    void findNonExistingByIdTest() {
        //arrange
        when(trainingTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        //act
        assertThatThrownBy(() -> trainingTypeService.findById(id))
                //assert
                .isInstanceOf(ScEntityNotFoundException.class)
                .hasMessage("TrainingType with id " + id + " doesn't exist", id);

    }
}