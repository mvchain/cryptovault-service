package com.mvc.cryptovault.common.bean;

import com.mvc.cryptovault.common.util.CryptoUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/12 13:13
 */
@Data
public class ExplorerBlockTransaction {

    final static Integer TX_MAX_NUMBER = 100000;

    @Id
    private BigInteger id;
    private String hash;
    private String blockId;
    private BigInteger fromUserId;
    private BigInteger toUserId;
    private Long createdAt;
    private BigDecimal value;
    private BigInteger tokenId;
    private String tokenName;

    public static Integer getIndex(BigInteger value) {
        Double val = Math.cos(value.doubleValue());
        val = val.equals(0D) ? 1 : val;
        int result = Math.abs((int) (TX_MAX_NUMBER * val) - value.intValue());
        result = result > TX_MAX_NUMBER ? result - TX_MAX_NUMBER : result;
        result = result > TX_MAX_NUMBER - 100 ? result - 100 : result;
        return result;
    }

    public static BigInteger getBlock(String hash) {
        if (StringUtils.isBlank(hash)) {
            return null;
        }
        String str = "";
        for (int i = 1; i < 44; i = i + 2) {
            str = str + hash.substring(i, i + 1);
        }
        return new BigInteger(CryptoUtil.decode(str)).subtract(BigInteger.valueOf(1546272000000L - 5000L)).divide(BigInteger.valueOf(5000));
    }

    public static void main(String[] args) {
        String str = "q1q2qFqtqhqyqWqeqzqrqyqmq-qQqmqTq9qtqaqtq8qwqqqqqqq";
        System.out.println(getBlock(str));
        System.out.println(getRealHash(str));
        System.out.println(combineHash(getBlock(str), getRealHash(str)));
    }

    public static String combineHash(BigInteger blockId, String hash) {
        String str = CryptoUtil.encode(blockId.multiply(BigInteger.valueOf(5000)).add(BigInteger.valueOf(1546272000000L - 5000L)).toString());
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            result = result + hash.charAt(i) + str.charAt(i);
        }
        return result + hash.substring(str.length());
    }

    public static String getRealHash(String hash) {
        if (StringUtils.isBlank(hash)) {
            return null;
        }
        String str = "";
        for (int i = 0; i < hash.length(); i++) {
            if ((i + 1) % 2 == 0 && i < 44) {
                continue;
            }
            str = str + hash.substring(i, i + 1);
        }
        return str;
    }

}
