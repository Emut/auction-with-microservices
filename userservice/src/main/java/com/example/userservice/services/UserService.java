package com.example.userservice.services;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto registerUser(UserDto userDto) {
        return new UserDto(userRepository.save(userDto.getUser()));
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDto::new).collect(Collectors.toList());
    }

    public UserDto getUser(long id) {
        return  new UserDto(userRepository.findById(id).orElse(null));
    }

    public UserDto login(String username, String password) {
        User user = userRepository.findByLoginAndPassword(username, password).orElse(null);
        if (user == null)
            return null;
        return new UserDto(user);
    }
}
