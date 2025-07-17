package com.example.repository;

import com.example.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDetails, Integer> {

    public Boolean existsByEmail(String email);

    public UserDetails findByEmail(String email);

    public List<UserDetails> findByRole(String role);

    public List<UserDetails> findByRoleContainingIgnoreCaseAndNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(String role, String name, String surname);

    public List<UserDetails> findByRoleContainingIgnoreCaseAndSurnameContainingIgnoreCase(String role, String surname);

    public List<UserDetails> findByRoleContainingIgnoreCaseAndNameContainingIgnoreCase(String role, String name);

    public List<UserDetails> findByRoleContainingIgnoreCase(String role);


}
