package com.mvc.cryptovault.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mvc.cryptovault.app.base.BaseTest;
import com.mvc.cryptovault.common.bean.dto.DebitDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import com.mvc.cryptovault.common.bean.vo.TokenBalanceVO;
import com.mvc.cryptovault.common.bean.vo.TransactionSimpleVO;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AssetControllerTest extends BaseTest {

    @Test
    public void getAsset() throws Exception {
        //返回的所有数据
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].value").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].ratio").isNotEmpty())
                .andReturn();
        List<TokenBalanceVO> data = parseObject(result, new TypeReference<List<TokenBalanceVO>>() {
        });
        BigDecimal value = data.stream().filter(obj -> obj.getTokenId().equals(BigInteger.valueOf(2))).collect(Collectors.toList()).get(0).getValue();
        //执行划账
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setPassword("123456");
        debitDTO.setValue(BigDecimal.ONE);
        result = mockMvc.perform(MockMvcRequestBuilders.post(host + "/asset/debit")
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(debitDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
        //再次查询余额扣减情况
        BigDecimal exceptValue = value.subtract(BigDecimal.ONE);
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].tokenId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].value").value(exceptValue.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].tokenName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].ratio").isNotEmpty())
                .andReturn();
    }

    @Test
    public void getBalance() throws Exception {
        //查询所有币种余额
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].value").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].ratio").isNotEmpty())
                .andReturn();
        List<TokenBalanceVO> data = parseObject(result, new TypeReference<List<TokenBalanceVO>>() {
        });
        BigDecimal sum = data.stream().map(obj -> obj.getRatio().multiply(obj.getValue())).reduce(BigDecimal.ZERO, BigDecimal::add);
        //检查余额是否正确
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset/balance")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").value(sum.toString()))
                .andReturn();
    }

    @Test
    public void getTransactions() throws Exception {
        //上拉获取所有转入数据,不能有非转入类型的数据
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset/transactions")
                .header("Authorization", getToken().getToken())
                .param("transactionType", "1")
                .param("id", "0")
                .param("pageSize", "99")
                .param("type", "0")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].transactionType").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].createdAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].updatedAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].value").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].ratio").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].status").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].classify").isNotEmpty())
                .andReturn();
        List<TransactionSimpleVO> data = parseObject(result, new TypeReference<List<TransactionSimpleVO>>() {
        });
        BigInteger upId = data.get(1).getId();
        BigInteger downId = data.get(data.size() - 2).getId();
        //分页上拉,应该有且只有1条数据
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset/transactions")
                .header("Authorization", getToken().getToken())
                .param("transactionType", "1")
                .param("id", upId.toString())
                .param("pageSize", "99")
                .param("type", "0")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data", hasSize(1)))
                .andReturn();
        //分页下拉,应该有且只有1条数据
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset/transactions")
                .header("Authorization", getToken().getToken())
                .param("transactionType", "1")
                .param("id", downId.toString())
                .param("pageSize", "99")
                .param("type", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data", hasSize(1)))
                .andReturn();
    }

    @Test
    public void getTransaction() throws Exception {
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset/transaction/1")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data.createdAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.updatedAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.status").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.fee").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.feeTokenType").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.hashLink").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.value").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.tokenName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.hash").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.toAddress").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.fromAddress").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.classify").isNotEmpty())
                .andReturn();
    }

    @Test
    public void getAddress() throws Exception {
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset/address")
                .header("Authorization", getToken().getToken())
                .param("tokenId", "3")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andReturn();
    }

    @Test
    public void debitBalance() throws Exception {
        //返回的所有余额
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].value").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].tokenName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].ratio").isNotEmpty())
                .andReturn();
        List<TokenBalanceVO> data = parseObject(result, new TypeReference<List<TokenBalanceVO>>() {
        });
        BigDecimal value = data.stream().filter(obj -> obj.getTokenId().equals(BigInteger.valueOf(2))).collect(Collectors.toList()).get(0).getValue();
        //余额中的余额和划账余额相等
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset/debit")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data").value(value.toString()))
                .andReturn();
    }

    @Test
    public void debit() throws Exception {
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setPassword("111111");
        debitDTO.setValue(BigDecimal.ONE);
        MvcResult result = null;
        //密码正确
        debitDTO.setPassword("123456");
        result = mockMvc.perform(MockMvcRequestBuilders.post(host + "/asset/debit")
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(debitDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();

    }

    @Test
    public void getTransactionInfo() throws Exception {
        //所有内容不可为空
        MvcResult result = null;
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset/transaction")
                .header("Authorization", getToken().getToken())
                .param("tokenId", "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.feeTokenName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.balance").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data.fee").isNotEmpty())
                .andReturn();
    }

    @Test
    public void sendTransaction() throws Exception {
        MvcResult result = null;
        Double value = Math.random();
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAddress("0x222");
        transactionDTO.setPassword("111");
        transactionDTO.setTokenId(BigInteger.valueOf(3));
        transactionDTO.setValue(BigDecimal.valueOf(value));
        //密码错误
        result = mockMvc.perform(MockMvcRequestBuilders.post(host + "/asset/transaction")
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(transactionDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();

        //预先获取余额
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andReturn();
        List<TokenBalanceVO> data = parseObject(result, new TypeReference<List<TokenBalanceVO>>() {
        });
        BigDecimal balance = data.stream().filter(obj -> obj.getTokenId().equals(BigInteger.valueOf(3))).collect(Collectors.toList()).get(0).getValue();
        //密码正确
        transactionDTO.setPassword("123456");
        result = mockMvc.perform(MockMvcRequestBuilders.post(host + "/asset/transaction")
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(transactionDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
        //检查交易列表中是否生成了订单（最新1条记录的交易额应该和随机生成的值相同）
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset/transactions")
                .header("Authorization", getToken().getToken())
                .param("transactionType", "1")
                .param("id", "0")
                .param("pageSize", "1")
                .param("type", "0")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].value", BigDecimal.class).value(value))
                .andReturn();
        //检查余额是否扣减
        result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/asset")
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("data[2].value", BigDecimal.class).value(balance.subtract(new BigDecimal(String.valueOf(value)))))
                .andReturn();
    }
}