package com.mvc.cryptovault.console.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AdminUser;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminPasswordDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminVO;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author qiyichen
 * @create 2018/11/21 16:37
 */
@RestController
@RequestMapping("dashboard/adminUser")
public class DAdminUserController extends BaseController {

    @GetMapping()
    public Result<PageInfo<AdminVO>> getAdmins(@ModelAttribute PageDTO dto) {
        List<AdminUser> list = adminUserService.findAll();
        List<AdminVO> vos = new ArrayList<>(list.size());
        list.forEach(obj -> {
            AdminVO vo = new AdminVO();
            BeanUtils.copyProperties(obj, vo);
            vos.add(vo);
        });
        PageInfo result = new PageInfo<>(list);
        result.setList(vos);
        return new Result<>(result);
    }

    @GetMapping("{id}")
    public Result<AdminDetailVO> getAdminDetail(@PathVariable BigInteger id) {
        AdminDetailVO result = adminUserService.getAdminDetail(id);
        return new Result<>(result);
    }

    @DeleteMapping("{id}")
    Result<Boolean> deleteAdmin(@RequestParam(value = "userId", required = false) BigInteger userId, @PathVariable("id") BigInteger id) {
        AdminUser admin = adminUserService.findById(userId);
        Assert.isTrue(admin.getAdminType() == 0 || userId.equals(id), "没有权限");
        adminUserService.deleteById(id);
        return new Result<>(true);
    }

    @GetMapping("password")
    public Result<Boolean> updatePwd(@RequestParam(value = "userId", required = false) BigInteger userId, @ModelAttribute AdminPasswordDTO adminPasswordDTO) {
        String key = "AdminUser".toUpperCase() + "_" + adminPasswordDTO.getUserId();
        AdminUser admin = adminUserService.findById(userId);
        Assert.isTrue(admin.getAdminType() == 0 || userId.equals(adminPasswordDTO.getUserId()), "没有权限");
        Assert.isTrue(admin.getPassword().equalsIgnoreCase(adminPasswordDTO.getPassword()), "密码错误");
        admin = adminUserService.findById(adminPasswordDTO.getUserId());
        adminUserService.update(admin);
        admin = adminUserService.findById(adminPasswordDTO.getUserId());
        redisTemplate.opsForValue().set(key, JSON.toJSONString(admin), 24, TimeUnit.HOURS);
        return new Result<>(true);
    }

    @GetMapping("username")
    public  Result<AdminUser> getAdminByUsername(@RequestParam(value = "username", required = false) String username) {
        AdminUser user = adminUserService.findOneBy("username", username);
        return new Result<>(user);
    }

    @PostMapping("")
    public   Result<Boolean> newAdmin(@RequestBody AdminDTO adminDTO) {
        adminUserService.newAdmin(adminDTO);
        return new Result<>(true);
    }

    @PutMapping("")
    public  Result<Boolean> updateAdmin(@RequestBody AdminDTO adminDTO) {
        adminUserService.updateAdmin(adminDTO);
        return new Result<>(true);
    }

    @GetMapping("balance")
    public Result<BigDecimal> getBalance(@RequestParam(value = "tokenId", required = false) BigInteger tokenId) {
        //TODO 区块链相关最后处理
        return new Result<>(new BigDecimal(6.6));
    }

}
