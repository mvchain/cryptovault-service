package com.mvc.cryptovault.app.util;


import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

/**
 * @author qiyichen
 * @create 2019/4/3 15:25
 */
public class GoogleAuthUtil {

    private final static String issuer = "TTPay";

    public static GoogleRegInfo createCredentials(String accountName, String secretKey) {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        final String secret = key.getKey();
        String otpAuthURL = String.format("otpauth://totp/%s:%s--%s?secret=%s&issuer=%s", issuer, issuer, accountName, secret, issuer);
        return new GoogleRegInfo(secret, otpAuthURL, null);
    }

    public static Boolean checkUser(String secret, int verificationCode) {
        GoogleAuthenticator ga = new GoogleAuthenticator();
        return ga.authorize(secret, verificationCode);
    }

//    public static void main(String[] args) {
//        createCredentials("aaa", null);
//        for (int i = 0; i < 10; i++) {
//            GoogleRegInfo info = createCredentials("aaa", "7WTUJ4TLDCYO3HRD");
//            System.out.println(JSON.toJSONString(info));
//        }
//    }

}
