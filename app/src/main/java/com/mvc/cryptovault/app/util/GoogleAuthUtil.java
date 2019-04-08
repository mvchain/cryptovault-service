package com.mvc.cryptovault.app.util;


import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * @author qiyichen
 * @create 2019/4/3 15:25
 */
public class GoogleAuthUtil {

    private final static String issuer = "TTPay";

    public static GoogleRegInfo createCredentials(String accountName, String secretKey) {
        GoogleAuthenticator googleAuthenticator = null;
        if (StringUtils.isNotBlank(secretKey)) {
            GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder().build();
            GoogleAuthenticatorKey googleAuthenticator2 = new GoogleAuthenticatorKey
                    .Builder(secretKey)
                    .setConfig(config)
                    .setVerificationCode(0)
                    .setScratchCodes(new ArrayList<Integer>())
                    .build();
            String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, issuer + "--" + accountName, googleAuthenticator2);
            return new GoogleRegInfo(googleAuthenticator2.getKey(), otpAuthURL);
        } else {
            googleAuthenticator = new GoogleAuthenticator();
        }
        final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        final String secret = key.getKey();
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, issuer + "--" + accountName, key);
        return new GoogleRegInfo(secret, otpAuthURL);
    }

    public static Boolean checkUser(String secret, int verificationCode) {
        GoogleAuthenticator ga = new GoogleAuthenticator();
        return ga.authorize(secret, verificationCode);
    }

//    public static void main(String[] args) {
//        createCredentials("aaa", null);
//        for(int i =0;i<10;i++){
//            GoogleRegInfo info = createCredentials("aaa", "7WTUJ4TLDCYO3HRD");
//            System.out.println(JSON.toJSONString(info));
//        }
//    }

}
