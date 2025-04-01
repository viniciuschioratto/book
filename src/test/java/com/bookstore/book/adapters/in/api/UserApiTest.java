package com.bookstore.book.adapters.in.api;

import com.bookstore.book.adapters.in.api.mapper.UserRequestMapper;
import com.bookstore.book.adapters.in.api.request.CreateUserRequest;
import com.bookstore.book.adapters.in.api.request.UpdateUserRequest;
import com.bookstore.book.adapters.in.api.response.UserResponse;
import com.bookstore.book.application.core.domain.UserDomain;
import com.bookstore.book.application.ports.in.UserCrudInputPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserApi.class)
@ExtendWith(SpringExtension.class)
public class UserApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserCrudInputPort userCrudInputPort;

    @MockitoBean
    private UserRequestMapper userRequestMapper;

    @DisplayName("Should Create User")
    @Test
    void should_createUser() throws Exception {

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name("Vinicius")
                .build();

        UserDomain userDomain = UserDomain.builder()
                .name("Vinicius")
                .build();

        UserResponse userResponse = UserResponse.builder()
                .name("Vinicius")
                .build();

        Mockito.when(userRequestMapper.fromCreateUserRequestToUserDomain(createUserRequest)).thenReturn(userDomain);
        Mockito.when(userCrudInputPort.createUser(userDomain)).thenReturn(userDomain);
        Mockito.when(userRequestMapper.fromUserDomainToUserResponse(userDomain)).thenReturn(userResponse);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/user/v1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createUserRequest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Vinicius")));

        Mockito.verify(userCrudInputPort, Mockito.times(1)).createUser(userDomain);
        Mockito.verifyNoMoreInteractions(userCrudInputPort);

        Mockito.verify(userRequestMapper, Mockito.times(1)).fromCreateUserRequestToUserDomain(ArgumentMatchers.any());
        Mockito.verify(userRequestMapper, Mockito.times(1)).fromUserDomainToUserResponse(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(userRequestMapper);

    }

    @DisplayName("Should Update User")
    @Test
    void should_updateUser() throws Exception {

        Long userId = 123L;

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .id(userId)
                .name("Vinicius")
                .build();

        UserDomain userDomain = UserDomain.builder()
                .id(userId)
                .name("Vinicius")
                .build();

        UserResponse userResponse = UserResponse.builder()
                .id(userId)
                .name("Vinicius")
                .build();

        Mockito.when(userRequestMapper.fromUpdateUserRequestToUserDomain(updateUserRequest)).thenReturn(userDomain);
        Mockito.when(userCrudInputPort.updateUser(userId, userDomain)).thenReturn(userDomain);
        Mockito.when(userRequestMapper.fromUserDomainToUserResponse(userDomain)).thenReturn(userResponse);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/user/v1/{bookId}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateUserRequest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Vinicius")));

        Mockito.verify(userCrudInputPort, Mockito.times(1)).updateUser(userId, userDomain);
        Mockito.verifyNoMoreInteractions(userCrudInputPort);

        Mockito.verify(userRequestMapper, Mockito.times(1)).fromUpdateUserRequestToUserDomain(ArgumentMatchers.any());
        Mockito.verify(userRequestMapper, Mockito.times(1)).fromUserDomainToUserResponse(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(userRequestMapper);

    }

    @DisplayName("Should Get User by email")
    @Test
    void should_getUserByEmail() throws Exception {

        String userEmail = "test@test.com";

        UserDomain userDomain = UserDomain.builder()
                .id(123L)
                .name("Vinicius")
                .build();

        UserResponse userResponse = UserResponse.builder()
                .id(123L)
                .name("Vinicius")
                .build();

        Mockito.when(userCrudInputPort.getUserByEmail(userEmail)).thenReturn(userDomain);
        Mockito.when(userRequestMapper.fromUserDomainToUserResponse(userDomain)).thenReturn(userResponse);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/user/v1/by-email")
                                .param("email", userEmail)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Vinicius")));

        Mockito.verify(userCrudInputPort, Mockito.times(1)).getUserByEmail(userEmail);
        Mockito.verifyNoMoreInteractions(userCrudInputPort);

        Mockito.verify(userRequestMapper, Mockito.times(1)).fromUserDomainToUserResponse(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(userRequestMapper);

    }

    @DisplayName("Should Delete User by Id")
    @Test
    void should_deleteUserById() throws Exception {

        Long userId = 123L;

        Mockito.doNothing().when(userCrudInputPort).deleteUser(userId);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/user/v1/{userId}", userId)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(userCrudInputPort, Mockito.times(1)).deleteUser(userId);
        Mockito.verifyNoMoreInteractions(userCrudInputPort);

    }
}
