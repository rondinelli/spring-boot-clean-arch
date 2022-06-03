package com.stt.solution.application.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class RegisterRequest {

    @NotNull
    private String name;
    @NotNull
    private String email;

    public RegisterRequest() {

    }
}
