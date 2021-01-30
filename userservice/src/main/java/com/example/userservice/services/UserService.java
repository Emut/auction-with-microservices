package com.example.userservice.services;

import com.example.userservice.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<UserDto> userDtoList = new ArrayList<>();

    public UserDto registerUser(UserDto userDto) {
        userDto.setId((long) userDtoList.size());
        userDtoList.add(userDto);
        return userDto;
    }

    public List<UserDto> getAllUsers() {
        return userDtoList;
    }

    public UserDto getUser(long id) {
        return userDtoList.stream().filter(userDto -> userDto.getId().equals(id))
                .findFirst().orElse(null);
    }

    public UserDto login(String username, String password) {
        return userDtoList.stream().filter(userDto -> userDto.getLogin().equals(username))
                .filter(userDto -> userDto.getPassword().equals(password))
                .findFirst().orElse(null);
    }
}
