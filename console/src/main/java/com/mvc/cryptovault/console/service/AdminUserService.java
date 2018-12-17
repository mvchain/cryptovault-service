package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AdminPermission;
import com.mvc.cryptovault.common.bean.AdminUser;
import com.mvc.cryptovault.common.bean.AdminUserPermission;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.PermissionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminDetailVO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AdminUserService extends AbstractService<AdminUser> implements BaseService<AdminUser> {

    @Autowired
    AdminUserPermissionService adminUserPermissionService;
    @Autowired
    AdminPermissionService adminPermissionService;

    public AdminDetailVO getAdminDetail(BigInteger id) {
        AdminDetailVO result = new AdminDetailVO();
        AdminUser admin = findById(id);
        BeanUtils.copyProperties(admin, result);
        PageHelper.clearPage();
        List<AdminUserPermission> permissions = adminUserPermissionService.findBy("userId", id);
        List<AdminPermission> allPermission = adminPermissionService.findAll();
        List<PermissionDTO> permissionList = new ArrayList<>(allPermission.size());
        StringBuilder permissionStr = new StringBuilder();
        for (AdminPermission permission : allPermission) {
            Boolean hasPermission = permissions.stream().anyMatch(obj -> obj.getPermissionId().equals(permission.getId()));
            permissionList.add(new PermissionDTO(permission.getId(), hasPermission ? 1 : 0));
            if (hasPermission) {
                permissionStr.append(permission.getId() + ",");
            }
        }
        result.setPermissionList(permissionList);
        result.setPermissions(permissionStr.toString());
        return result;
    }

    public void newAdmin(AdminDTO adminDTO) {
        Assert.isNull(findOneBy("username", adminDTO.getUsername()), "用户名已存在");
        Long time = System.currentTimeMillis();
        AdminUser adminUser = new AdminUser();
        adminUser.setCreatedAt(time);
        adminUser.setUpdatedAt(time);
        adminUser.setNickname(adminDTO.getNickname());
        adminUser.setPassword(adminDTO.getPassword());
        adminUser.setUsername(adminDTO.getUsername());
        adminUser.setAdminType(1);
        adminUser.setStatus(adminDTO.getStatus());
        save(adminUser);
        updateAllCache();
        updateCache(adminUser.getId());
        adminUserPermissionService.updatePermission(adminUser.getId(), adminDTO.getPermissionList());
    }

    public void updateAdmin(AdminDTO adminDTO) {
        AdminUser user = findOneBy("username", adminDTO.getUsername());
        Assert.isTrue(null == user || user.getId().equals(adminDTO.getId()), "用户名已存在");
        Long time = System.currentTimeMillis();
        AdminUser adminUser = new AdminUser();
        adminUser.setUpdatedAt(time);
        adminUser.setNickname(adminDTO.getNickname());
        adminUser.setUsername(adminDTO.getUsername());
        adminUser.setStatus(adminDTO.getStatus());
        adminUser.setId(adminDTO.getId());
        update(adminUser);
        String key = "AdminUser".toUpperCase() + "_" + adminUser.getId();
        redisTemplate.opsForValue().set(key, JSON.toJSONString(adminUser), 24, TimeUnit.HOURS);
        adminUserPermissionService.updatePermission(adminUser.getId(), adminDTO.getPermissionList());
    }
}