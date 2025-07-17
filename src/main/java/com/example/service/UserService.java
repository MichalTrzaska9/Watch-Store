package com.example.service;

import com.example.entity.UserDetails;

import java.util.List;

public interface UserService {

    public UserDetails saveUser(UserDetails userDetails);

    public UserDetails getUserByEmail(String email);

    public List<UserDetails> getUsers(String role);

    public List<UserDetails> searchUsers(String role, String name, String surname);

    public Boolean updateAccountStatus(Integer id, Boolean status);

    public UserDetails editUser(UserDetails userDetails);

    public UserDetails editUserAccount(UserDetails userDetails);

    public UserDetails saveAdmin(UserDetails userDetails);

    public Boolean existsUser(String email);
}


