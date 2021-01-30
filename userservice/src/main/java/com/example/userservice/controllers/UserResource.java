package com.example.userservice.controllers;

import com.example.userservice.dto.UserDto;
import com.example.userservice.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public UserDto registerUser(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @GetMapping("/user/{id}")
    public UserDto getUser(@PathVariable("id") long id) {
        return userService.getUser(id);
    }

    @GetMapping("/users")
    public List<UserDto> getAllUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/login")
    public UserDto login(@RequestBody UserDto userDto){
        return userService.login(userDto.getLogin(), userDto.getPassword());
    }
}
