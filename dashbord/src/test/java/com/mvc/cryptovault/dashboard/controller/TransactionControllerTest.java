package com.mvc.cryptovault.dashboard.controller;

import com.mvc.cryptovault.common.dashboard.bean.vo.DTransactionVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.OverTransactionVO;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class TransactionControllerTest extends BaseTest {

    String controller = "/transaction";

    @Test
    public void findTransaction() throws Exception {
        String url = host + controller + "";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .param("pairId", "")
                .param("transactionType", "")
                .param("orderNumber", "")
                .param("cellphone", "")
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
        addNullActionTest(action, "data.list[0]", DTransactionVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void cancel() throws Exception {
        String url = host + controller + "/1";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void transactionExport() throws Exception {
        String url = host + controller + "/excel";

    }

    @Test
    public void overList() throws Exception {
        String url = host + controller + "/over";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .param("pairId", "")
                .param("transactionType", "")
                .param("orderNumber", "")
                .param("cellphone", "")
                .param("parentOrderNumber", "")
                .param("pageSize", "1")
                .param("pageNum", "1")
                .param("updatedStartAt", "")
                .param("orderBy", "id desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        action.andExpect(MockMvcResultMatchers.jsonPath("data.list").isNotEmpty());
        addNullActionTest(action, "data.list[0]", OverTransactionVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void overTransactionExport() throws Exception {
        String url = host + controller + "/over/excel";

    }
}