package com.stt.solution.application.rest;


import com.stt.solution.application.request.RegisterRequest;
import com.stt.solution.domain.TO.PaginationTO;
import com.stt.solution.domain.entity.User;
import com.stt.solution.domain.TO.PageTO;
import com.stt.solution.domain.exceptions.DomainException;
import com.stt.solution.domain.service.DomainUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/v1/user")
@RestController
public class UserController {

    @Autowired
    private DomainUserService domainUserService;

    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest request) {

        try {
            return ResponseEntity.ok(domainUserService.register(request));
        } catch (DomainException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String name,
                                 @RequestParam(required = false) String email, @RequestParam(value = "from") @DateTimeFormat(pattern = "dd/MM/yyyy") Date from) {

        PaginationTO paginationTO = new PaginationTO(page, size);
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        paginationTO.setFrom(from);
        paginationTO.setParams(params);
        Page<User> users = domainUserService.getUsers(paginationTO);

        return ResponseEntity.ok(new PageTO<User>(users.getContent(), users.getTotalElements()));

    }


}
