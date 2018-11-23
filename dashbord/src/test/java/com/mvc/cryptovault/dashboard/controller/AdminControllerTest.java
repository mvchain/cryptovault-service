package com.mvc.cryptovault.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminPasswordDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.PermissionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminVO;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AdminControllerTest extends BaseTest {

    @Test
    public void getAdmins() throws Exception {
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(host + "/admin")
                .header("Authorization", getToken().getToken())
                .param("pageNum", "1")
                .param("pageSize", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data.list").isNotEmpty());
        addNullActionTest(action, "data.list[0]", AdminVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void getAdminDetail() throws Exception {
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(host + "/admin/1")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        addNullActionTest(action, "data", AdminDetailVO.class, "permissionList");
        addNullActionTest(action, "data.permissionList[0]", PermissionDTO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void newAdmin() throws Exception {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setUsername("test" + Math.random());
        adminDTO.setNickname("test");
        adminDTO.setPassword("admin");
        adminDTO.setStatus(1);
        adminDTO.setPermissionList(Arrays.asList(new PermissionDTO[]{new PermissionDTO(BigInteger.valueOf(1), 1), new PermissionDTO(BigInteger.valueOf(2), 1),}));
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(host + "/admin")
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(adminDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void updateAdmin() throws Exception {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(BigInteger.valueOf(1));
        adminDTO.setUsername("admin");
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.put(host + "/admin/1")
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(adminDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void deleteAdmin() throws Exception {
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.delete(host + "/admin/2")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void updatePwd() throws Exception {
        AdminPasswordDTO data = new AdminPasswordDTO();
        data.setNewPassword("admin");
        data.setPassword("admin");
        data.setUserId(BigInteger.valueOf(1));
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.put(host + "/admin/1/password")
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void login() throws Exception {
        getToken();
    }

    @Test
    public void refresh() throws Exception {
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(host + "/admin/refresh")
                .header("Authorization", getToken().getRefreshToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void getExportSign() throws Exception {
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(host + "/admin/export/sign")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }
}