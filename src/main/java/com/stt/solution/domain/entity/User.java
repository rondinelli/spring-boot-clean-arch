package com.stt.solution.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
public class User extends BaseEntity {


    private String email;

    @JsonIgnore
    private String password;

    private String name;

    @Column(name = "activation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivationStatus activationStatus;

    @JsonIgnore
    @Column(name = "activation_token", unique = true)
    private String activationToken;

    @Column(name="user_type_name")
    private String userTypeName;

    @JsonIgnore
    @Column(name = "activation_token_exp")
    private Date activationTokenExp;

    public User() {

    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public enum ActivationStatus {
        INACTIVATED,
        WAITING_ACTIVATION,
        ACTIVATED
    }

}
