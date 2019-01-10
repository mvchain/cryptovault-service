package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.AppUserInvite;
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
    public Result<List<RecommendVO>> getRecommend(@RequestBody RecommendDTO dto){
        List<RecommendVO> result = appUserInviteService.getRecommend(dto);
        return new Result<>(result);
    }

    @PutMapping()
    public Result<Boolean> updateUser(@RequestBody AppUser user){
        user.setUpdatedAt(System.currentTimeMillis());
        appUserService.update(user);
        return new Result<>(true);
    }

    @GetMapping("pvKey")
    public Result<AppUser> getUserByPvKey(@RequestParam String value) {
        AppUser user = appUserService.findOneBy("pvKey", value);
        return new Result<>(user);
    }

    @PostMapping("password")
    public Result<Boolean> forget(@RequestParam("userId") BigInteger userId, @RequestParam("password") String password) {
        AppUser user = appUserService.findById(userId);
        if (null != user) {
            user.setPassword(password);
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
            //如果存在值且不为空,则用户存在,直接获取
            user = appUserService.findById(NumberUtils.createBigInteger(result));
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
}