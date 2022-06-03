package com.stt.solution.domain.repository;


import com.stt.solution.domain.TO.PaginationTO;
import com.stt.solution.domain.entity.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserRepository {

    Page<User> findAll(PaginationTO paginationTO);

    Optional<User> findByEmail(String email);

    User save(User user);

}
