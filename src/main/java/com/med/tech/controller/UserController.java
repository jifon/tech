package com.med.tech.controller;

import com.med.tech.dto.CodeRequestDTO;
import com.med.tech.dto.UserDTO;
import com.med.tech.dto.UserResetDTO;
import com.med.tech.entity.User;
import com.med.tech.dto.AuthenticationRequestDTO;
import com.med.tech.exeption.ResourceNotFoundException;
import com.med.tech.repository.UserRepository;
import com.med.tech.security.jwt.JwtTokenProvider;
import com.med.tech.service.SmsService;
import com.med.tech.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

//    -----------------------------------------------------------------------------
    private final AuthenticationManager authenticationManager;      // делегировать
    private UserRepository userRepository;                          //   все
    private JwtTokenProvider jwtTokenProvider;                      //    сервису
//    ------------------------------------------------------------------------------
    @Autowired
    private SmsService sms;
    @Autowired
    private UserServiceImpl userService;

    public UserController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/registration")
    public void sendUser(@RequestBody UserDTO user) throws Exception {

        //если номер телефона еще не зареган
        if(!userService.existsByEmail(user.getEmail())){
            sms.sendOtp(user);
        }
        else throw new Exception("Пользователь уже зарегистрирован!");

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {

        //проверка: пользователь существует и незаблокированный
        if((userService.existsByEmail(request.getEmail())) && userService.isActive(request.getEmail())){

                try {
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                    User user = userService.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
                    String token = jwtTokenProvider.createToken(request.getEmail(), user.getRole().name());
                    Map<Object, Object> response = new HashMap<>();
                    response.put("email", request.getEmail());
                    response.put("token", token);
                    if(user.getOtpActive()){
                        user.setOtpActive(false);
                        userService.updateUser(user);
                    }
                    return ResponseEntity.ok(response);

                } catch (AuthenticationException e) {
                    return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
                }
        } else {
            return new ResponseEntity<>("Пользователь не существует", HttpStatus.NOT_FOUND);
        }

    }



    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }


    //1) Запрос 4 значного кода при авторизации
    @PostMapping("/forgot-password")
    public void auth(@RequestBody UserResetDTO email) {
        if((userService.existsByEmail(email.getEmail())) && userService.isActive(email.getEmail())){
            Optional<User> user = userRepository.findByEmail(email.getEmail());
            sms.sendCode(user.get().getPhoneNumber());
        }
        else{
            throw new ResourceNotFoundException("User not found");}
    }
    
    //2)
    //подтверждение кода
    @PostMapping("/verification")
    public ResponseEntity<?> activate(@RequestBody CodeRequestDTO requestDTO) throws Exception {
        if((userService.existsByEmail(requestDTO.getMail())) && userService.isActive(requestDTO.getMail())){
            if(userService.matchesCode(requestDTO)){

                User user = userService.findByEmail(requestDTO.getMail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
                String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
                Map<Object, Object> response = new HashMap<>();
                response.put("email", user.getEmail());
                response.put("token", token);
                return ResponseEntity.ok(response);
            }
            else{
                return new ResponseEntity<>("wrong code", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }
    }






}
