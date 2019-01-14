package com.mvc.cryptovault.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.bean.vo.TokenVO;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminPasswordDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DUserDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminVO;
import com.mvc.cryptovault.common.permission.NotLogin;
import com.mvc.cryptovault.dashboard.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@Api(tags = "管理员相关")
@RestController
@RequestMapping("admin")
public class AdminController extends BaseController {

    @Autowired
    private OssService ossService;
    @ApiOperation("获取所有管理员")
    @GetMapping()
    public Result<PageInfo<AdminVO>> getAdmins(@ModelAttribute @Valid PageDTO dto) {
        return new Result<>(adminService.getAdmins(dto));
    }

    @ApiOperation("获取管理员详情")
    @GetMapping("{id}")
    public Result<AdminDetailVO> getAdminDetail(@PathVariable BigInteger id) {
        return new Result<>(adminService.getAdminDetail(id));
    }

    @ApiOperation("新建管理员")
    @PostMapping
    public Result<Boolean> newAdmin(@RequestBody @Valid AdminDTO adminDTO) {
        Boolean result = adminService.newAdmin(adminDTO);
        return new Result<>(true);
    }

    @ApiOperation("编辑管理员(子管理员的权限修改将被忽略)")
    @PutMapping("{id}")
    public Result<Boolean> updateAdmin(@PathVariable BigInteger id, @RequestBody @Valid AdminDTO adminDTO) {
        Boolean result = adminService.updateAdmin(adminDTO);
        return new Result<>(true);
    }

    @ApiOperation("删除管理员(禁用)")
    @DeleteMapping("{id}")
    public Result<Boolean> deleteAdmin(@PathVariable BigInteger id) {
        Boolean result = adminService.deleteAdmin(id);
        return new Result<>(true);
    }

    @ApiOperation("修改密码")
    @PutMapping("{id}/password")
    public Result<Boolean> updatePwd(@PathVariable BigInteger id, @RequestBody @Valid AdminPasswordDTO adminPasswordDTO) {
        Boolean result = adminService.updatePwd(id, adminPasswordDTO);
        return new Result<>(true);
    }

    @ApiOperation("用户登录,缓存登录令牌.登录规则后续确定")
    @PostMapping("login")
    @NotLogin
    public Result<TokenVO> login(HttpServletResponse response, @RequestBody @Valid DUserDTO userDTO) {
        TokenVO tokenVO = adminService.login(userDTO);
        return new Result<>(tokenVO);
    }

    @PostMapping("refresh")
    @ApiOperation("刷新令牌")
    Result<String> refresh() {
        String newToken = adminService.refresh();
        return new Result<>(newToken);
    }

    @ApiOperation("获取下载签名,只能使用一次,5分钟内有效,一个签名只能使用一次")
    @GetMapping("export/sign")
    public Result<String> getExportSign() {
        String sign = adminService.getSign();
        return new Result<>(sign);
    }

    @ApiOperation("获取oss签名")
    @GetMapping("signature")
    Result<Map> doGetSignature(@RequestParam String dir) throws UnsupportedEncodingException {
        return new Result<>(ossService.doGetSignature(dir));
    }

}
