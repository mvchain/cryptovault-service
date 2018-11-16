package com.mvc.cryptovault.app.controller;

import com.alibaba.fastjson.TypeReference;
import com.mvc.cryptovault.app.base.BaseTest;
import com.mvc.cryptovault.common.bean.vo.MessageVO;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class MessageControllerTest extends BaseTest {

    /**
     * FIXME 这里需要根据预先插入的数据进行条件设置。有时间修改为动态生成
     *
     * @throws Exception
     */
    @Test
    public void getlist() throws Exception {
        MvcResult result = null;
        //上拉,预先插入若干数据,需返最新数据
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/message")
                .header("Authorization", getToken().getToken())
                .param("pageSize", "2")
                .param("timestamp", "0")
                .param("type", SEARCH_DIRECTION_UP.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].status").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].message").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].messageType").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].messageId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].read").isNotEmpty())
                .andReturn();
        List<MessageVO> data = parseObject(result, new TypeReference<List<MessageVO>>() {
        });
        Long min = data.get(data.size() - 1).getCreatedAt();
        Long max = data.get(0).getCreatedAt();
        //测试是否还有更新的数据
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/message")
                .header("Authorization", getToken().getToken())
                .param("pageSize", "2")
                .param("timestamp", max.toString())
                .param("type", SEARCH_DIRECTION_UP.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isEmpty())
                .andReturn();
        //测试更老的数据
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/message")
                .header("Authorization", getToken().getToken())
                .param("pageSize", "2")
                .param("timestamp", min.toString())
                .param("type", SEARCH_DIRECTION_DOWN.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andReturn();
    }

    @Test
    public void read() throws Exception {
        MvcResult result = null;
        //上拉,预先插入若干数据,需返最新数据
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/message")
                .header("Authorization", getToken().getToken())
                .param("pageSize", "2")
                .param("timestamp", "0")
                .param("type", SEARCH_DIRECTION_UP.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].status").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].message").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].messageType").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].messageId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].read").isNotEmpty())
                .andReturn();
        List<MessageVO> data = parseObject(result, new TypeReference<List<MessageVO>>() {
        });
        BigInteger id = data.get(0).getId();
        //修改指定id的记录为已读
        result = mockMvc.perform(MockMvcRequestBuilders.put(host + "/message/" + id)
                .header("Authorization", getToken().getToken())
                .param("type", SEARCH_DIRECTION_UP.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andReturn();
        //FIXME 检查已读状态,后续修改为低优先队列,无法立刻获取已读状态,需要客户端记录
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/message")
                .header("Authorization", getToken().getToken())
                .param("pageSize", "2")
                .param("timestamp", "0")
                .param("type", SEARCH_DIRECTION_UP.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].read").value("1"))
                .andReturn();
    }

}