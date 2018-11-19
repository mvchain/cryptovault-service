package com.mvc.cryptovault.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mvc.cryptovault.app.base.BaseTest;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.bean.vo.MyOrderVO;
import com.mvc.cryptovault.common.bean.vo.OrderInfoVO;
import com.mvc.cryptovault.common.bean.vo.OrderVO;
import org.hamcrest.Matchers;
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

public class TransactionControllerTest extends BaseTest {

    @Test
    public void getPair() throws Exception {
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction/pair")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andReturn();
    }

    @Test
    public void getTransactions() throws Exception {
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction")
                .header("Authorization", getToken().getToken())
                .param("pairId", "1")
                .param("transactionType", "1")
                .param("id", "0")
                .param("type", "0")
                .param("pageSize", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].headImage").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].limitValue").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].nickname").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].total").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].id").isNotEmpty())
                .andReturn();
        var data = parseObject(result, new TypeReference<List<OrderVO>>() {
        });
        BigInteger upId = data.get(1).getId();
        BigInteger downId = data.get(data.size() - 2).getId();
        //上拉
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction")
                .header("Authorization", getToken().getToken())
                .param("pairId", "1")
                .param("transactionType", "1")
                .param("id", upId.toString())
                .param("type", "0")
                .param("pageSize", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data", Matchers.hasSize(1)))
                .andReturn();
        //下拉
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction")
                .header("Authorization", getToken().getToken())
                .param("pairId", "1")
                .param("transactionType", "1")
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
    public void getKLine() throws Exception {
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction/pair/kline")
                .header("Authorization", getToken().getToken())
                .param("pairId", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andReturn();
    }

    @Test
    public void getUserTransactions() throws Exception {
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction/partake")
                .header("Authorization", getToken().getToken())
                .param("pairId", "1")
                .param("status", "0")
                .param("transactionType", "1")
                .param("id", "0")
                .param("type", "0")
                .param("pageSize", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
        var data = parseObject(result, new TypeReference<List<MyOrderVO>>() {
        });
        BigInteger upId = data.get(1).getId();
        BigInteger downId = data.get(data.size() - 2).getId();
        //上拉
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction/partake")
                .header("Authorization", getToken().getToken())
                .param("pairId", "1")
                .param("status", "0")
                .param("transactionType", "1")
                .param("id", upId.toString())
                .param("type", "0")
                .param("pageSize", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data", Matchers.hasSize(1)))
                .andReturn();
        //下拉
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction/partake")
                .header("Authorization", getToken().getToken())
                .param("pairId", "1")
                .param("status", "0")
                .param("transactionType", "1")
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
    public void buy() throws Exception {
        MvcResult result = null;
        Double value = Math.random();
        TransactionBuyDTO dto = new TransactionBuyDTO();
        dto.setPassword("123456");
        dto.setId(BigInteger.ZERO);
        dto.setPairId(BigInteger.ONE);
        dto.setPrice(BigDecimal.ONE);
        dto.setTransactionType(1);
        dto.setValue(BigDecimal.valueOf(value));
        //查询挂单信息
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction/info")
                .header("Authorization", getToken().getToken())
                .param("transactionType", "1")
                .param("pairId", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.balance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.tokenBalance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.price").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.min").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.max").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.value").isNotEmpty())
                .andReturn();
        OrderInfoVO vo = parseObject(result, OrderInfoVO.class);
        //直接挂空买单
        result = mockMvc.perform(MockMvcRequestBuilders.post(host + "/transaction")
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(dto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
        //检查挂单后资金变动
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction/info")
                .header("Authorization", getToken().getToken())
                .param("transactionType", "1")
                .param("pairId", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                //购买后基础币种余额 = 初始余额-（购买数量*购买价格）
                .andExpect(MockMvcResultMatchers.jsonPath("data.balance", BigDecimal.class).value(vo.getBalance().subtract(BigDecimal.valueOf(value).multiply(dto.getPrice()))))
                .andExpect(MockMvcResultMatchers.jsonPath("data.tokenBalance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.price").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.min").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.max").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.value").isNotEmpty())
                .andReturn();
    }

    @Test
    public void getInfo() throws Exception {
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction/info")
                .header("Authorization", getToken().getToken())
                .param("transactionType", "1")
                .param("pairId", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.balance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.tokenBalance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.price").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.min").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.max").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.value").isNotEmpty())
                .andReturn();
    }

    @Test
    public void cancel() throws Exception {
        MvcResult result = null;
        //检查余额
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction/info")
                .header("Authorization", getToken().getToken())
                .param("transactionType", "1")
                .param("pairId", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.balance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.tokenBalance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.price").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.min").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.max").isNotEmpty())
                .andReturn();
        OrderInfoVO vo = parseObject(result, OrderInfoVO.class);
        BigDecimal balance = vo.getBalance();
        //取消挂单
        result = mockMvc.perform(MockMvcRequestBuilders.delete(host + "/transaction/5")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
        //检查余额是否重置()
        //检查余额
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/transaction/info")
                .header("Authorization", getToken().getToken())
                .param("transactionType", "1")
                .param("pairId", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.balance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.tokenBalance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.price").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.min").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.max").isNotEmpty())
                .andReturn();
    }
}