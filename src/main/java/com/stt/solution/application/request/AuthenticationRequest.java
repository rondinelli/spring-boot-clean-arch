package com.stt.solution.application.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class AuthenticationRequest {

    private String username;
    private String password;
}
