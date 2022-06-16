package com.med.tech.service;

import com.med.tech.dto.CodeRequestDTO;
import com.med.tech.dto.UserDTO;
import com.med.tech.entity.User;
import com.med.tech.entity.constants.Status;
import com.med.tech.exeption.ResourceNotFoundException;
import com.med.tech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;


    @Override
    public void createUser(UserDTO model) {
        User user = new User(model.getFirstName(), model.getLastName(),model.getEmail(), model.getPhoneNumber(), model.getRole());
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> deleteUser(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isActive(String email) {
        Optional<User> u = userRepository.findByEmail(email);
        return u.get().getStatus() == Status.ACTIVE;
    }

    @Override
    public boolean loginViaOtp(String email) {
        Optional<User> u = userRepository.findByEmail(email);
        boolean otpStatus = u.get().getOtpActive();
        return otpStatus;
    }

    @Override
    public boolean matchesCode(CodeRequestDTO codeRequestDTO) throws Exception {
        User user = userRepository.findByEmail(codeRequestDTO.getMail()).orElseThrow(
                ()-> new ResourceNotFoundException("пользователь с таким номером не существует!"));


//      Убедитесь, что закодированный пароль, полученный из хранилища,
//      совпадает с отправленным необработанным паролем после того,
//      как он также будет закодирован.

        if (encoder.matches(codeRequestDTO.getCode(), user.getPswResetCode())) {
            return true;
        }
        else{
            throw new Exception("Неправильный код!");}

    }

//    private User toEntity(UserModel userModel) {
//        User user = new User();
//        user.setId(userModel.getId());
//        user.setFirstName(userModel.getFirstName());
//        user.setLastName(userModel.getLastName());
//        user.setEmail(userModel.getEmail());
//        user.setRole(userModel.getRole());
//        user.setStatus(userModel.getStatus());
//        user.setPassword(userModel.getPassword());
//
//        return user;
//    }
}
