package com.med.tech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.med.tech.entity.constants.Role;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Role role;

}
