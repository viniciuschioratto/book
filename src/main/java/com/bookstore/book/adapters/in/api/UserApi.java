package com.bookstore.book.adapters.in.api;

import com.bookstore.book.adapters.in.api.mapper.UserRequestMapper;
import com.bookstore.book.adapters.in.api.request.CreateUserRequest;
import com.bookstore.book.adapters.in.api.request.UpdateUserRequest;
import com.bookstore.book.adapters.in.api.response.ExceptionResponse;
import com.bookstore.book.adapters.in.api.response.UserResponse;
import com.bookstore.book.application.core.domain.UserDomain;
import com.bookstore.book.application.core.exceptions.UserNotFoundException;
import com.bookstore.book.application.ports.in.UserCrudInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user/v1")
@Tag(name = "User API V1", description = "This API exposes resources about User Domain")
public class UserApi {
    private final UserCrudInputPort userCrudInputPort;
    private final UserRequestMapper userRequestMapper;

    public UserApi(
            UserCrudInputPort userCrudInputPort,
            UserRequestMapper userRequestMapper
    ) {
        this.userCrudInputPort = userCrudInputPort;
        this.userRequestMapper = userRequestMapper;
    }

    @Operation(summary = "Create User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to create User", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "500", description = "Internal error - The server faced issues to resolve the request", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        UserDomain userDomain = userCrudInputPort.createUser(userRequestMapper.fromCreateUserRequestToUserDomain(createUserRequest));
        return ResponseEntity.ok(userRequestMapper.fromUserDomainToUserResponse(userDomain));
    }

    @Operation(summary = "Update User by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to update User", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "500", description = "Internal error - The server faced issues to resolve the request", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
    })
    @PutMapping("{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("userId") Long userId, @RequestBody UpdateUserRequest updateUserRequest) throws UserNotFoundException {
        UserDomain userDomain = userCrudInputPort.updateUser(userId, userRequestMapper.fromUpdateUserRequestToUserDomain(updateUserRequest));
        return ResponseEntity.ok(userRequestMapper.fromUserDomainToUserResponse(userDomain));
    }

    @Operation(summary = "Get User by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User returned successfully", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to return User", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "500", description = "Internal error - The server faced issues to resolve the request", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
    })
    @GetMapping("by-email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam("email") String email) throws UserNotFoundException {
        UserDomain userDomain = userCrudInputPort.getUserByEmail(email);
        return ResponseEntity.ok(userRequestMapper.fromUserDomainToUserResponse(userDomain));
    }

    @Operation(summary = "Delete User by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Error to delete book", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
            @ApiResponse(responseCode = "500", description = "Internal error - The server faced issues to resolve the request", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            }),
    })
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteBookById(@PathVariable("userId") Long userId) throws UserNotFoundException {
        userCrudInputPort.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
