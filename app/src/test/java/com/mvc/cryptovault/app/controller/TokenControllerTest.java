package com.mvc.cryptovault.app.controller;

import com.alibaba.fastjson.TypeReference;
import com.mvc.cryptovault.app.base.BaseTest;
import com.mvc.cryptovault.common.bean.vo.TokenDetailVO;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class TokenControllerTest extends BaseTest {

    @Test
    public void getTokens() throws Exception {
        //查询所有
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/token")
                .header("Authorization", getToken().getToken())
                .param("timestamp", "0")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenImage").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenCnName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenEnName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenId").exists())
                .andReturn();
        //增量更新,取最后一条数据的前一个时间戳,返回结果应该为1条
        List<TokenDetailVO> data = parseObject(result, new TypeReference<List<TokenDetailVO>>() {
        });
        Long timestamp = data.get(data.size() - 2).getTimestamp();
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/token")
                .header("Authorization", getToken().getToken())
                .param("timestamp", timestamp.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data", hasSize(1)))
                .andReturn();
    }

    @Test
    public void getBase() throws Exception {
        //查询所有
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/token/base")
                .header("Authorization", getToken().getToken())
                .param("timestamp", "0")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].ratio").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenId").exists())
                .andReturn();
    }

}