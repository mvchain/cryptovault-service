package com.mvc.cryptovault.testdb.util;

import com.mvc.cryptovault.testdb.util.bip39.*;

import java.util.List;

/**
 * @author qiyichen
 * @create 2019/1/10 10:33
 */
public class MnemonicUtil {

    private final static Words DEFAULT_WORDS_LEN = Words.TWELVE;
    private final static MnemonicCode mnemonicCode = new MnemonicCode();

    public static String getRandomCode() {
        return getRandomCode(DEFAULT_WORDS_LEN);
    }

    public static String getRandomCode(Words words) {
        byte[] random = RandomSeed.random(words);
        return HexUtils.toHex(random);
    }

    public static List<String> getWordsList(String key) {
        byte[] hex = HexUtils.fromHex(key);
        try {
            return mnemonicCode.toMnemonic(hex);
        } catch (MnemonicException.MnemonicLengthException e) {
            return null;
        }
    }

    public static Boolean equals(String key, List<String> value) {
        try {
            byte[] result = mnemonicCode.toEntropy(value);
            return HexUtils.toHex(result).equals(key);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getPvKey(List<String> asList) {
        try {
            byte[] result = mnemonicCode.toEntropy(asList);
            return HexUtils.toHex(result);
        } catch (Exception e) {
            return null;
        }
    }

}
