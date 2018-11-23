package com.mvc.cryptovault.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectOrderVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectVO;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ProjectControllerTest extends BaseTest {

    String controller = "/project";

    @Test
    public void projects() throws Exception {
        String url = host + controller + "";
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
        addNullActionTest(action, "data.list[0]", DProjectVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void getDetail() throws Exception {
        String url = host + controller + "/1";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        addNullActionTest(action, "data", DProjectDetailVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void newProject() throws Exception {
        DProjectDTO dto = new DProjectDTO();
        dto.setBaseTokenId(BigInteger.ONE);
        dto.setBaseTokenName("Vrt");
        dto.setProjectImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542378065226&di=513e584258d0bed94aea1be2b0fd5f11&imgtype=0&");
        dto.setProjectLimit(BigDecimal.valueOf(1000));
        dto.setProjectName(("测试项目" + Math.random()).substring(0, 15));
        dto.setProjectTotal(BigDecimal.valueOf(500000));
        dto.setRatio(0.02f);
        dto.setReleaseValue(0.01f);
        dto.setStartedAt(System.currentTimeMillis());
        dto.setStopAt(System.currentTimeMillis());
        dto.setVisiable(0);
        dto.setTokenId(BigInteger.valueOf(4));
        dto.setTokenName("USDT");
        String url = host + controller + "";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(dto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void updateProject() throws Exception {
        DProjectDTO dto = new DProjectDTO();
        dto.setId(BigInteger.ONE);
        dto.setBaseTokenId(BigInteger.ONE);
        dto.setBaseTokenName("Vrt");
        dto.setProjectImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542378065226&di=513e584258d0bed94aea1be2b0fd5f11&imgtype=0&");
        dto.setProjectLimit(BigDecimal.valueOf(1000));
        dto.setProjectName(("测试项目" + Math.random()).substring(0,15));
        dto.setProjectTotal(BigDecimal.valueOf(500000));
        dto.setRatio(0.02f);
        dto.setReleaseValue(0.01f);
        dto.setStartedAt(System.currentTimeMillis());
        dto.setStopAt(System.currentTimeMillis());
        dto.setVisiable(0);
        dto.setTokenId(BigInteger.valueOf(4));
        dto.setTokenName("USDT");
        String url = host + controller + "";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.put(url)
                .header("Authorization", getToken().getToken())
                .content(JSON.toJSONString(dto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void deleteProject() throws Exception {
        String url = host + controller + "/0";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void findOrders() throws Exception {
        String url = host + controller + "/order";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", getToken().getToken())
                .param("projectName", "")
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
        addNullActionTest(action, "data.list[0]", DProjectOrderVO.class);
        MvcResult result = action.andReturn();
    }

    @Test
    public void cancelOrder() throws Exception {
        String url = host + controller + "/order/1";
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .header("Authorization", getToken().getToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        action.andExpect(MockMvcResultMatchers.jsonPath("data").isNotEmpty());
        MvcResult result = action.andReturn();
    }

    @Test
    public void overTransactionExport() throws Exception {
        String url = host + controller + "/order/excel";
    }
}