package com.stt.solution.infra.repository;

import com.stt.solution.domain.TO.PaginationTO;
import com.stt.solution.domain.entity.User;
import com.stt.solution.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.*;

@Repository
public class PostgresDbUserRepository implements UserRepository {


    private final SpringDataUserRepository userRepository;

    @Autowired
    public PostgresDbUserRepository(SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> findAll(PaginationTO paginationTO) {
        Pageable pageable = PageRequest.of(paginationTO.getPage(), paginationTO.getSize());

        Map<String, Object> params = paginationTO.getParams();
        User user = User.builder().name((String) params.get("name")).email((String)params.get("email")).build();

        Page<User> users = userRepository.findAll(getSpecFromDatesAndExample(paginationTO.getFrom(), paginationTO.getTo(), Example.of(user)), pageable);
        return users;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public User save(User user) {
       return userRepository.save(user);
    }

    private Specification<User> getSpecFromDatesAndExample(
            Date from, Date to, Example<User> example) {

        return (Specification<User>) (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (from != null) {
                predicates.add(builder.greaterThan(root.get("created"), from));
            }
            if (to != null) {
                predicates.add(builder.lessThan(root.get("created"), to));
            }
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));

            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    };
}
