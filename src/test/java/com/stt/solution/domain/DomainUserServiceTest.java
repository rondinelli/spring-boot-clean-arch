package com.stt.solution.domain;//package com.stt.solution.controller.service;

import com.stt.solution.domain.entity.User;
import com.stt.solution.domain.repository.UserRepository;
import com.stt.solution.domain.service.DomainUserService;
import com.stt.solution.domain.util.EmailService;
import com.stt.solution.domain.util.EncryptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.*;

public class DomainUserServiceTest {

    private UserRepository userRepository;
    private DomainUserService tested;
    private EncryptService encryptService;
    private EmailService emailService;
    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        tested = new DomainUserService(userRepository, encryptService, emailService);
    }

    @Test
     void shouldCreateUser_thenSaveIt() {
        final User user = new User("email", "name");
        when(userRepository.save(user)).thenReturn(user);
        User savedUser = tested.create(user);

        assertNotNull(savedUser.getEmail());
    }

}
