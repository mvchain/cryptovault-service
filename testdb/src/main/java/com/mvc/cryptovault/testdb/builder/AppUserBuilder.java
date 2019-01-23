package com.mvc.cryptovault.testdb.builder;


import com.mvc.cryptovault.testdb.util.EncryptionUtil;
import com.mvc.cryptovault.testdb.util.MnemonicUtil;

/**
 * @author qiyichen
 * @create 2019/1/19 16:03
 */
public class AppUserBuilder extends BaseBuilder {

    public static final Integer NUMBER = 100000;


    private String getCode() {
        String result = MnemonicUtil.getRandomCode();
        return result;
    }

    @Override
    public String getInstance(Integer id) {
        String email = "qiyic" + id + "@qq.com";
        String password = "";
        try {
//            password = EncryptionUtil.md5(email + EncryptionUtil.md5("123456"));
            password = "123456";
        } catch (Exception e) {
        }
        return String.format("(%s, '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, '%s', '%s', %s)",
                id, "test", password, "test", password, email, 10, 0, 1, 0, email, getCode(), 0);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_user` (`id`, `cellphone`, `password`, `head_image`, `transaction_password`, `nickname`, `created_at`, `updated_at`, `status`, `invite_level`, `email`, `pv_key`, `invite_num`) VALUES ";
    }

    public @Override
    String tableName() {
        return "app_user";
    }
}
