package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.AppUserDTO;
import com.mvc.cryptovault.common.bean.dto.RecommendDTO;
import com.mvc.cryptovault.common.bean.vo.AppUserRetVO;
import com.mvc.cryptovault.common.bean.vo.RecommendVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.AppUserInviteService;
import com.mvc.cryptovault.console.service.AppUserService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mvc.cryptovault.common.constant.RedisConstant.APP_USER_USERNAME;

/**
 * @author qiyichen
 * @create 2018/11/12 14:35
 */
@RequestMapping("user")
@RestController
public class UserController extends BaseController {

    @Autowired
    AppUserService appUserService;
    @Autowired
    AppUserInviteService appUserInviteService;

    @GetMapping("recommend")
    public Result<List<RecommendVO>> getRecommend(@ModelAttribute RecommendDTO dto) {
        List<RecommendVO> result = appUserInviteService.getRecommend(dto);
        return new Result<>(result);
    }

    @PutMapping()
    public Result<Boolean> updateUser(@RequestBody AppUser user) {
        AppUser userTemp = appUserService.findById(user.getId());
        Object obj = redisTemplate.opsForHash().get(APP_USER_USERNAME + userTemp.getEmail(), "SIGN_DATE");
        redisTemplate.opsForHash().delete(APP_USER_USERNAME + userTemp.getEmail(), APP_USER_USERNAME + userTemp.getEmail());
        user.setUpdatedAt(System.currentTimeMillis());
        appUserService.update(user);
        user = appUserService.findById(user.getId());
        String key = APP_USER_USERNAME + user.getEmail();
        if (null != obj) {
            redisTemplate.opsForHash().putIfAbsent(key, "SIGN_DATE", String.valueOf(obj));
        }
        redisTemplate.opsForHash().put(key, key, String.valueOf(user.getId()));
        appUserService.updateCache(user.getId());
        return new Result<>(true);
    }

    @GetMapping("pvKey")
    public Result<AppUser> getUserByPvKey(@RequestParam String value) {
        AppUser user = appUserService.findOneBy("pvKey", value);
        return new Result<>(user);
    }

    @PostMapping("password")
    public Result<Boolean> forget(@RequestParam("userId") BigInteger userId, @RequestParam("password") String password, @RequestParam("type") Integer type) {
        AppUser user = appUserService.findById(userId);
        if (null != user) {
            if (type == 1) {
                user.setPassword(password);
            } else if (type == 2) {
                user.setTransactionPassword(password);
            }
            appUserService.update(user);
            appUserService.updateCache(userId);
        }
        return new Result<>(true);
    }

    @PostMapping("mnemonics")
    public Result<Boolean> mnemonicsActive(@RequestParam String email) {
        appUserService.mnemonicsActive(email);
        return new Result<>(true);
    }

    @PostMapping()
    public Result<AppUserRetVO> register(@RequestBody AppUserDTO appUserDTO) {
        AppUserRetVO vo = appUserService.register(appUserDTO);
        return new Result<>(vo);
    }

    @GetMapping("username")
    public Result<AppUser> getByUsername(@RequestParam String username) {
        String key = APP_USER_USERNAME + username;
        String result = (String) redisTemplate.opsForHash().get(key, key);
        AppUser user = null;
        if (null == result) {
            user = appUserService.findOneBy("email", username);
            if (null == user) {
                //用户不存在则保存空串,防止缓存穿透
                redisTemplate.opsForHash().put(key, key, "");
                redisTemplate.expire(key, 10, TimeUnit.MINUTES);
            } else {
                redisTemplate.opsForHash().put(key, key, String.valueOf(user.getId()));
            }
        } else if (!"".equals(result)) {
            user = appUserService.findById(NumberUtils.createBigInteger(result));
            //如果存在值且不为空,则用户存在,直接获取
            if (!username.equals(user.getEmail())) {
                redisTemplate.delete(key);
                return new Result<>();
            }
        }
        return new Result<>(user);
    }

    @GetMapping("{id}")
    public Result<AppUser> getUserById(@PathVariable BigInteger id) {
        return new Result(appUserService.findById(id));
    }

    @GetMapping("{id}/tag")
    public Result<String> getTag(@PathVariable("id") BigInteger userId) {
        String result = appUserService.getTag(userId);
        return new Result<>(result);
    }

    @PutMapping("sign")
    Result<Boolean> sign(@RequestParam("userId") BigInteger userId) {
        if (getSign(userId).getData()) {
            return new Result<>(true);
        }
        AppUser user = appUserService.findById(userId);
        String key = APP_USER_USERNAME + user.getEmail();
        redisTemplate.opsForHash().delete(key, "SIGN_DATE");
        Boolean result = redisTemplate.opsForHash().putIfAbsent(key, "SIGN_DATE", getNowDate());
        if (result) {
            appUserService.sign(userId);
        }
        return new Result<>(result);
    }

    @GetMapping("sign")
    Result<Boolean> getSign(@RequestParam("userId") BigInteger userId) {
        AppUser user = appUserService.findById(userId);
        String key = APP_USER_USERNAME + user.getEmail();
        String result = (String) redisTemplate.opsForHash().get(key, "SIGN_DATE");
        return new Result<>(null != result && result.equals(getNowDate()));
    }

    private String getNowDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String str = sdf.format(calendar.getTime());
        return str;
    }

}