package com.mvc.cryptovault.app.controller;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdControllerTest {

    final static String host = "http://localhost:10086";

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Rule
    public ContiPerfRule rule = new ContiPerfRule();

    @Before
    public void setup() {
        //让每个测试用例启动之前都构建这样一个启动项
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @PerfTest(threads = 40, duration = 10000)
    public void test1() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(host + "/ad")
                //请求参数
                //请求编码和数据格式为json和UTF8
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                //期望的返回值 或者返回状态码
                .andExpect(MockMvcResultMatchers.status().isOk());
//        System.out.printf(result.andReturn().getResponse().getContentAsString());
    }

}