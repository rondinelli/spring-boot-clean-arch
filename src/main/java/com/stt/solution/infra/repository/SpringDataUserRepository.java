package com.stt.solution.infra.repository;

import com.stt.solution.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {

    User findByEmail(String email);

}
