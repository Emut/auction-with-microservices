package com.example.userservice.dto;

import com.example.userservice.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDto {
    private String name;
    private String login;
    private String password;
    private boolean isAdmin = false;
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public User getUser() {
        User retval = new User();
        retval.setId(id);
        retval.setAdmin(isAdmin);
        retval.setLogin(login);
        retval.setName(name);
        retval.setPassword(password);
        return retval;
    }

}
