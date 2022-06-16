package com.med.tech.dto;

import lombok.Data;

@Data

public class OtpGenerator {
    int min = 1000;
    int max = 9999;

    int code = (int) (Math.random()*(max-min+1)+min);

}
