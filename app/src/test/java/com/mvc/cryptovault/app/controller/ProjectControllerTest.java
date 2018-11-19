package com.mvc.cryptovault.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mvc.cryptovault.app.base.BaseTest;
import com.mvc.cryptovault.common.bean.dto.ProjectBuyDTO;
import com.mvc.cryptovault.common.bean.vo.ProjectBuyVO;
import com.mvc.cryptovault.common.bean.vo.ProjectSimpleVO;
import com.mvc.cryptovault.common.bean.vo.PurchaseVO;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ProjectControllerTest extends BaseTest {

    @Test
    public void getProject() throws Exception {
        MvcResult result = null;
        //查询所有项目
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/project")
                .header("Authorization", getToken().getToken())
                .param("projectType", "0")
                .param("projectId", "0")
                .param("type", "0")
                .param("pageSize", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
        List<ProjectSimpleVO> data = parseObject(result, new TypeReference<List<ProjectSimpleVO>>() {
        });
        Long num = data.stream().filter(obj -> obj.getStatus() != 0).count();
        BigInteger upId = data.get(1).getProjectId();
        BigInteger downId = data.get(data.size() - 2).getProjectId();
        Assert.assertTrue(num == 0l);
        //上拉
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/project")
                .header("Authorization", getToken().getToken())
                .param("projectType", "0")
                .param("projectId", upId.toString())
                .param("type", "0")
                .param("pageSize", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data", Matchers.hasSize(1)))
                .andReturn();
        //下拉
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/project")
                .header("Authorization", getToken().getToken())
                .param("projectType", "0")
                .param("projectId", downId.toString())
                .param("type", "1")
                .param("pageSize", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data", Matchers.hasSize(1)))
                .andReturn();
    }

    @Test
    public void getReservation() throws Exception {
        MvcResult result = null;
        //查询所有项目
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/project/reservation")
                .header("Authorization", getToken().getToken())
                .param("id", "0")
                .param("type", "0")
                .param("pageSize", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
        List<PurchaseVO> data = parseObject(result, new TypeReference<List<PurchaseVO>>() {
        });
        BigInteger upId = data.get(1).getId();
        BigInteger downId = data.get(data.size() - 2).getId();
        //上拉
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/project/reservation")
                .header("Authorization", getToken().getToken())
                .param("id", upId.toString())
                .param("type", "0")
                .param("pageSize", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data", Matchers.hasSize(1)))
                .andReturn();
        //下拉
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/project/reservation")
                .header("Authorization", getToken().getToken())
                .param("id", downId.toString())
                .param("type", "1")
                .param("pageSize", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data", Matchers.hasSize(1)))
                .andReturn();
    }

    @Test
    public void getPurchaseInfo() throws Exception {
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/project/1/purchase")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data.balance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.limitValue").isNotEmpty())
                .andReturn();
    }

    @Test
    public void buy() throws Exception {
        ProjectBuyDTO dto = new ProjectBuyDTO();
        dto.setPassword("123456");
        dto.setValue(BigDecimal.valueOf(0.01));
        MvcResult result = null;
        //获取可购买余额
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/project/1/purchase")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data.balance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.limitValue").isNotEmpty())
                .andReturn();
        ProjectBuyVO data = parseObject(result, ProjectBuyVO.class);
        BigDecimal limit = data.getLimitValue();
        BigDecimal exceptValue = limit.subtract(BigDecimal.valueOf(0.01));
        //购买
        result = mockMvc.perform(MockMvcRequestBuilders.post(host + "/project/1/purchase")
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(dto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
        //检查可购买余额
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/project/1/purchase")
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(dto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data.balance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.limitValue", BigDecimal.class).value(exceptValue))
                .andReturn();
    }
}