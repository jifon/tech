package com.med.tech.service;

import com.med.tech.dto.CodeRequestDTO;
import com.med.tech.dto.UserDTO;
import com.med.tech.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void createUser(UserDTO user);
    List<User> getAllUsers();
    public void updateUser(User user);
    Optional<User> deleteUser(long id);

    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean isActive(String email);
    boolean loginViaOtp(String email);
    boolean matchesCode(CodeRequestDTO codeRequestDTO) throws Exception;
}
