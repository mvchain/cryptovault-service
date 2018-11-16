package com.mvc.cryptovault.app.controller;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.app.base.BaseTest;
import com.mvc.cryptovault.common.bean.dto.UserDTO;
import com.mvc.cryptovault.common.bean.vo.TokenVO;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UserControllerTest extends BaseTest {

    @Test
    public void login() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("123456");
        userDTO.setUsername("18888888888");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(host + "/user/login")
                .content(JSON.toJSONString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.userId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data.token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data.refreshToken").exists())
                .andReturn();
    }

    @Test
    public void getInfo() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/user/info")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data.nickname").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data.username").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data.headImage").exists())
                .andReturn();
    }

    @Test
    public void refresh() throws Exception {
        TokenVO vo = getToken();
        //使用错误的token刷新
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.post(host + "/user/refresh")
                .header("Authorization", vo.getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()))
                .andReturn();
        //使用正确的token
        result = mockMvc.perform(MockMvcRequestBuilders.post(host + "/user/refresh")
                .header("Authorization", vo.getRefreshToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").exists())
                .andReturn();
        String newToken = parseObject(result, String.class);
        //验证正确的token
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/user/info")
                .header("Authorization", newToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data.nickname").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data.username").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data.headImage").exists())
                .andReturn();
    }

}