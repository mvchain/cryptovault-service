package com.mvc.cryptovault.app.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleRegInfo {
    private String secret;
    private String otpAuthURL;
}