package com.mvc.cryptovault.app.util;


import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

/**
 * @author qiyichen
 * @create 2019/4/3 15:25
 */
public class GoogleAuthUtil {

    private final static String issuer = "TTPay";

    public static GoogleRegInfo createCredentials(String accountName) {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key =
                googleAuthenticator.createCredentials();
        final String secret = key.getKey();
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, issuer + "--" + accountName, key);
        return new GoogleRegInfo(secret, otpAuthURL);
    }

    public static Boolean checkUser(String secret, int verificationCode) {
        GoogleAuthenticator ga = new GoogleAuthenticator();
        return ga.authorize(secret, verificationCode);
    }

}
