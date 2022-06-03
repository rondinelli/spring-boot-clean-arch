package com.stt.solution.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.stt.solution.application.request.RegisterRequest;
import com.stt.solution.domain.service.DomainUserService;
import org.hamcrest.Matchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.stt.solution.application.rest.UserController;
import com.stt.solution.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class, useDefaultFilters = false)
@Import(UserController.class)
public class UserControllerTests {

    @MockBean
    DomainUserService domainUserService;

    @Autowired
    MockMvc mockMvc;

    @Configuration
    @EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class})
    static class ContextConfiguration { }

    @TestConfiguration
    static class DefaultConfigWithoutCsrf extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll();
        }
    }

    @Test
    public void testRegister() throws Exception {
        User user = new User("joao132@gmail.com", "João da Silva");
        user.setActivationStatus(User.ActivationStatus.WAITING_ACTIVATION);

        RegisterRequest request = new RegisterRequest("João da Silva", "joao132@gmail.com");

        Mockito.when(domainUserService.register(request)).thenReturn(user);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(request);

        mockMvc.perform(post("/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.is("joao132@gmail.com")));

    }

    @Test
    public void returns400_WhenRemoveEmailFromRequest() throws Exception {
        User user = new User();
        user.setEmail("joao132@gmail.com");

        user.setActivationStatus(User.ActivationStatus.WAITING_ACTIVATION);

        RegisterRequest request = new RegisterRequest();
        request.setName("João da Silva");

        Mockito.when(domainUserService.register(request)).thenReturn(user);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(request);

        mockMvc.perform(post("/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


}
