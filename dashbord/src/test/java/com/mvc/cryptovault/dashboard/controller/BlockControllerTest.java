package com.mvc.cryptovault.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockeTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DBlockeTransactionVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DHoldVO;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class BlockControllerTest extends BaseTest {

    String controller = "/block";

    @Test
    public void getHold() throws Exception {
        String url = host + controller + "/hold";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        addNullActionTest(action, "data[0]", DHoldVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void setHold() throws Exception {
        String url = host + controller + "/hold";
        List<DHoldVO> list = Arrays.asList(new DHoldVO("ETH", BigInteger.valueOf(3), BigDecimal.valueOf(0.1)), new DHoldVO("USDT", BigInteger.valueOf(4), BigDecimal.valueOf(0.1)));
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(list))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void getFee() throws Exception {
        String url = host + controller + "/fee";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        addNullActionTest(action, "data[0]", DHoldVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void setFee() throws Exception {
        String url = host + controller + "/fee";
        List<DHoldVO> list = Arrays.asList(new DHoldVO("ETH", BigInteger.valueOf(3), BigDecimal.valueOf(0.1)), new DHoldVO("USDT", BigInteger.valueOf(4), BigDecimal.valueOf(0.1)));
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(list))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void getTransactions() throws Exception {
        String url = host + controller + "/transactions";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .param("orderNumber", "")
                .param("toAddress", "")
                .param("status", "")
                .param("pageSize", "1")
                .param("pageNum", "1")
                .param("updatedStartAt", "")
                .param("orderBy", "id desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        action.andExpect(MockMvcResultMatchers.jsonPath("data.list").isNotEmpty());
        addNullActionTest(action, "data.list[0]", DBlockeTransactionVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void getTransactionsExcel() throws Exception {
        String url = host + controller + "hold";

    }

    @Test
    public void getBalance() throws Exception {
        String url = host + controller + "hold";

    }

    @Test
    public void updateStatus() throws Exception {
        String url = host + controller + "hold";

    }

    @Test
    public void exportSign() throws Exception {
        String url = host + controller + "hold";

    }

    @Test
    public void importSign() throws Exception {
        String url = host + controller + "hold";
    }

    @Test
    public void exportSign1() throws Exception {
        String url = host + controller + "hold";
    }

    @Test
    public void importAccount() throws Exception {
        String url = host + controller + "hold";


    }

    @Test
    public void accountCount() throws Exception {
        String url = host + controller + "hold";

    }
}