package com.nikolabojanic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nikolabojanic.dto.UserPasswordChangeRequestDto;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private Counter totalTransactionsCounter;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void findByUsernameTest() {
        //arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));
        //act
        UserEntity user = userService.findByUsername(RandomStringUtils.randomAlphabetic(5));
        //assert
        assertThat(user).isNotNull();
    }

    @Test
    void findByNonExistingUsernameTest() {
        //arrange
        String username = RandomStringUtils.randomAlphabetic(10);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        //act
        assertThatThrownBy(() -> userService.findByUsername(username))
            .isInstanceOf(ScEntityNotFoundException.class)
            .hasMessage("User with username " + username + " doesn't exist", username);
    }

    @Test
    void changeUserPasswordTest() {
        //arrange
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        requestDto.setNewPassword(RandomStringUtils.randomAlphabetic(10));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));
        when(passwordEncoder.encode(anyString())).thenReturn("");
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        //act
        UserEntity user = userService.changeUserPassword(
            RandomStringUtils.randomAlphabetic(10),
            requestDto);
        //assert
        assertThat(user).isNotNull();
    }

    @Test
    void generateUniqueUsernameAndPasswordTest() {
        //arrange
        UserEntity newUser = new UserEntity();
        newUser.setFirstName("Nikola");
        newUser.setLastName("Bojanic");
        when(userRepository.countUsersWithSameName(
            newUser.getFirstName(),
            newUser.getLastName())).thenReturn(3L);
        //act
        UserEntity user = userService.generateUsernameAndPassword(newUser);
        //assert
        assertThat(user.getUsername()).isEqualTo("Nikola.Bojanic4");
        assertThat(user.getPassword()).hasSize(10);
    }

    @Test
    void generateExistingUsernameAndPasswordTest() {
        //arrange
        UserEntity newUser = new UserEntity();
        newUser.setFirstName("Nikola");
        newUser.setLastName("Bojanic");
        when(userRepository.countUsersWithSameName(
            newUser.getFirstName(),
            newUser.getLastName())).thenReturn(0L);
        //act
        UserEntity user = userService.generateUsernameAndPassword(newUser);
        //assert
        assertThat(user.getUsername()).isEqualTo("Nikola.Bojanic");
        assertThat(user.getPassword()).hasSize(10);
    }
}