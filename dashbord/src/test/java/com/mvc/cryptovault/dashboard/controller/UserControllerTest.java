package com.mvc.cryptovault.dashboard.controller;

import com.mvc.cryptovault.common.dashboard.bean.dto.DUSerVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUSerDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserBalanceVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserLogVO;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UserControllerTest extends BaseTest {
    String controller = "/user";

    @Test
    public void findUser() throws Exception {
        String url = host + controller + "";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .param("cellphone", "")
                .param("pageSize", "1")
                .param("pageNum", "1")
                .param("updatedStartAt", "")
                .param("orderBy", "id desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        action.andExpect(MockMvcResultMatchers.jsonPath("data.list").isNotEmpty());
        addNullActionTest(action, "data.list[0]", DUSerVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void getUserDetail() throws Exception {
        String url = host + controller + "/1";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        addNullActionTest(action, "data", DUSerDetailVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void getBalance() throws Exception {
        String url = host + controller + "/1/balance";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .param("pageSize", "1")
                .param("pageNum", "1")
                .param("updatedStartAt", "")
                .param("orderBy", "id desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        addNullActionTest(action, "data[0]", DUserBalanceVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void getUserLog() throws Exception {
        String url = host + controller + "/1/log";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .param("pageSize", "1")
                .param("pageNum", "1")
                .param("updatedStartAt", "")
                .param("orderBy", "id desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        action.andExpect(MockMvcResultMatchers.jsonPath("data.list").isNotEmpty());
        addNullActionTest(action, "data.list[0]", DUserLogVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void userLogExport() throws Exception {
        String url = host + controller + "/1/log/excel";

    }
}