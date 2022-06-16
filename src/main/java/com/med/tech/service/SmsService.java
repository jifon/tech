package com.med.tech.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.med.tech.dto.OtpGenerator;
import com.med.tech.dto.UserDTO;
import com.med.tech.entity.User;
import com.med.tech.entity.constants.Status;
import com.med.tech.exeption.ResourceNotFoundException;
import com.med.tech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class
SmsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final String ACCOUNT_SID = "AC8e7bd30ddc7d00f2854f6479acf148b6";

    private final String AUTH_TOKEN = "81e9d5c24132dfd8ce33eadf53b2553e";

    private final String FROM_NUMBER = "+13517776596";

    public boolean sendOtp(UserDTO model) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            OtpGenerator codeGeneration = new OtpGenerator();

            User newUser = new User();
            newUser.setLastName(model.getLastName());
            newUser.setFirstName(model.getFirstName());
            newUser.setEmail(model.getEmail());
            newUser.setPhoneNumber(model.getPhoneNumber());
            newUser.setRole(model.getRole());
            newUser.setStatus(Status.ACTIVE);
            newUser.setPassword(encoder.encode(codeGeneration.getCode() + ""));
            newUser.setOtpActive(true);
            userRepository.save(newUser);

            String code = codeGeneration.getCode() + " - this is your otp code.";
            Message message = Message.creator(new PhoneNumber(model.getPhoneNumber()),
                            new PhoneNumber(FROM_NUMBER), code)
                    .create();
            System.out.println("message sent to number - " + model.getPhoneNumber() + " generated code - " + code);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void sendCode(String phone) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        OtpGenerator codeGeneration = new OtpGenerator();

        User us = userRepository.findByPhoneNumber(phone).orElseThrow(
                () -> new ResourceNotFoundException(
                        "пользователь с таким номером не существует!")
        );
        String code = codeGeneration.getCode() + "";
        us.setPswResetCode(encoder.encode(code));
        userRepository.save(us);
        String sms = codeGeneration.getCode() + " - this is your otp code.";
        Message message = com.twilio.rest.api.v2010.account.Message.creator(new PhoneNumber(phone),
                        new PhoneNumber(FROM_NUMBER), sms)
                .create();

        System.out.println("message sent to number - " + phone + " generated code - " + code);
    }
}

