package com.mvc.cryptovault.dashboard.controller;

import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.dto.UserDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.bean.vo.TokenVO;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminPasswordDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminVO;
import com.mvc.cryptovault.common.permission.NotLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@Api(tags = "管理员相关")
@RestController
@RequestMapping("admin")
public class AdminController extends BaseController {

    @ApiOperation("获取所有管理员")
    @GetMapping()
    public Result<AdminVO> getAdmins(@ModelAttribute @Valid PageDTO dto) {
        return null;
    }

    @ApiOperation("获取管理员详情")
    @GetMapping("{id}")
    public Result<AdminDetailVO> getAdminDetail(@PathVariable BigInteger id) {
        return null;
    }

    @ApiOperation("新建管理员")
    @PostMapping
    public Result<Boolean> newAdmin(@RequestBody @Valid AdminDTO adminDTO) {
        return null;
    }

    @ApiOperation("编辑管理员(子管理员的权限修改将被忽略)")
    @PutMapping("{id}")
    public Result<Boolean> updateAdmin(@PathVariable BigInteger id, @RequestBody @Valid AdminDTO adminDTO) {
        return null;
    }

    @ApiOperation("删除管理员(禁用)")
    @DeleteMapping("{id}")
    public Result<Boolean> deleteAdmin(@PathVariable BigInteger id) {
        return null;
    }

    @ApiOperation("修改密码")
    @PutMapping("{id}/password")
    public Result<Boolean> updatePwd(@PathVariable BigInteger id, @RequestBody @Valid AdminPasswordDTO adminPasswordDTO) {
        return null;
    }

    @ApiOperation("用户登录,缓存登录令牌.登录规则后续确定")
    @PostMapping("login")
    @NotLogin
    public Result<TokenVO> login(HttpServletResponse response, @RequestBody @Valid UserDTO userDTO) {
        return null;
    }

    @PostMapping("refresh")
    @ApiOperation("刷新令牌")
    @NotLogin
    Result<String> refresh() {
        return null;
    }

    @ApiOperation("获取下载签名,只能使用一次,5分钟内有效,一个签名只能使用一次")
    @GetMapping("export/sign")
    public Result<String> getExportSign() {
        return null;
    }

}
