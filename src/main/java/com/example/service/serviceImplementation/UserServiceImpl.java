package com.example.service.serviceImplementation;

import com.example.entity.UserDetails;
import com.example.repository.UserRepository;
import com.example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails saveUser(UserDetails userDetails) {
        userDetails.setRole("ROLE_USER");
        userDetails.setIsEnable(true);
        String encodePassword = passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(encodePassword);
        return userRepository.save(userDetails);
    }

    @Override
    public List<UserDetails> getUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public UserDetails getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public List<UserDetails> searchUsers(String role, String name, String surname) {
        name = StringUtils.hasLength(name) ? name : "";
        surname = StringUtils.hasLength(surname) ? surname : "";

        if (!surname.isEmpty() && name.isEmpty()) {
            List<UserDetails> usersBySurname = userRepository.findByRoleContainingIgnoreCaseAndSurnameContainingIgnoreCase(role, surname);
            if (!usersBySurname.isEmpty()) {
                return usersBySurname;
            } else {
                return userRepository.findByRoleContainingIgnoreCaseAndNameContainingIgnoreCase(role, surname);
            }
        }

        if (!name.isEmpty() && !surname.isEmpty()) {
            return userRepository.findByRoleContainingIgnoreCaseAndNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(role, name, surname);
        }

        if (!name.isEmpty()) {
            return userRepository.findByRoleContainingIgnoreCaseAndNameContainingIgnoreCase(role, name);
        }

        return userRepository.findByRoleContainingIgnoreCase(role);
    }


    @Override
    public Boolean updateAccountStatus(Integer id, Boolean status) {
        Optional<UserDetails> findByUser = userRepository.findById(id);

        if (findByUser.isPresent()) {
            UserDetails userDetails = findByUser.get();
            userDetails.setIsEnable(status);
            userRepository.save(userDetails);
            return true;
        }
        return false;
    }


    @Override
    public UserDetails editUser(UserDetails userDetails) {
        return userRepository.save(userDetails);
    }

    @Override
    public UserDetails editUserAccount(UserDetails userDetails) {

        UserDetails user = userRepository.findById(userDetails.getId()).get();

        if (!ObjectUtils.isEmpty(user)) {
            user.setName(userDetails.getName());
            user.setSurname(userDetails.getSurname());
            user.setPhone(userDetails.getPhone());
            user.setStreet(userDetails.getStreet());
            user.setCity(userDetails.getCity());
            user.setCountry(userDetails.getCountry());
            user.setPostcode(userDetails.getPostcode());
            user = userRepository.save(user);

        }
        return user;
    }

    @Override
    public UserDetails saveAdmin(UserDetails userDetails) {
        userDetails.setRole("ROLE_ADMIN");
        userDetails.setIsEnable(true);
        String encodePassword = passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(encodePassword);
        return userRepository.save(userDetails);
    }

    @Override
    public Boolean existsUser(String email) {
        return userRepository.existsByEmail(email);
    }


}
