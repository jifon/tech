package com.med.tech.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
public class AuthenticationRequestDTO {
    private String email;
    private String password;
}
