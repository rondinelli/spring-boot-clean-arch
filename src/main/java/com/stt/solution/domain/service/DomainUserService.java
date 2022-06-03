package com.stt.solution.domain.service;

import com.stt.solution.application.request.RegisterRequest;
import com.stt.solution.domain.TO.PaginationTO;
import com.stt.solution.domain.entity.User;
import com.stt.solution.domain.exceptions.DomainException;
import com.stt.solution.domain.exceptions.ErrorCode;
import com.stt.solution.domain.repository.UserRepository;
import com.stt.solution.domain.util.EmailService;
import com.stt.solution.domain.util.EncryptService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DomainUserService  {

    private final Integer ACTIVATION_EXP = 7;

    private final UserRepository userRepository;

    private final EncryptService encryptService;

    private final EmailService emailService;


    public DomainUserService(UserRepository userRepository, EncryptService encryptService, EmailService emailService) {
        this.userRepository = userRepository;
        this.encryptService = encryptService;
        this.emailService = emailService;
    }

    public Page<User> getUsers(PaginationTO paginationTO) {
        return userRepository.findAll(paginationTO);
    }

    public Optional<User> findByEmail(String name) {
        return userRepository.findByEmail(name);
    }


    public User register(RegisterRequest request) throws DomainException {
        Optional<User> repeatedUser = this.findByEmail(request.getEmail());
        if (repeatedUser.isPresent()) throw new DomainException(ErrorCode.REPEATED);

        String activationToken = encryptService.hashString(request.getEmail());
        LocalDateTime expLocalDate = LocalDateTime.now();
        expLocalDate = expLocalDate.plusDays(ACTIVATION_EXP);
        Date expDate = Date.from(expLocalDate.atZone(ZoneId.systemDefault()).toInstant());

        User newUser = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .activationToken(activationToken)
                .activationStatus(User.ActivationStatus.WAITING_ACTIVATION)
                .activationTokenExp(expDate).build();


        Map<String, String> context = new HashMap<>();
        context.put("name", newUser.getName());
        context.put("url", "http://wwww");

        this.emailService.sendMail(newUser.getEmail(), "Welcome!", "register-template.html", context);

        return this.create(newUser);
    }

    @Transactional()
    public User create(User user) {

        return userRepository.save(user);
    }


}
