package com.stt.solution.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponseError {

    private List<Error> errors;

    public ResponseError(){
        errors = new ArrayList<>();
    }

    public void addError(Error error){
        errors.add(error);
    }
}
