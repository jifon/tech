package com.med.tech.entity;

import lombok.*;
import com.med.tech.entity.constants.Role;
import com.med.tech.entity.constants.Status;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users", schema = "public", catalog = "construction")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name ="first_name",nullable = false)
    private String firstName;
    @Column(name ="last_name",nullable = false)
    private String lastName;
    @Column(name = "email",nullable = false, unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name ="phone_number",nullable = false)
    private String phoneNumber;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @Column(name = "otp_active" ,columnDefinition = "boolean default true" )
    private boolean otpActive;
    @Column(name = "psw_reset_code")
    private String pswResetCode;

    public User(String firstName, String lastName, String email, String phoneNumber, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }


    public boolean getOtpActive() {
        return this.otpActive;
    }
}
