package com.stt.solution.repository;

import com.stt.solution.domain.entity.User;
import com.stt.solution.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testRepository(){
        User user = new User("email", "name");
        user.setActivationStatus(User.ActivationStatus.ACTIVATED);
        userRepository.save(user);
        Optional<User> userSave =  userRepository.findByEmail("email");
        assertNotNull(userSave.get());
    }
}
