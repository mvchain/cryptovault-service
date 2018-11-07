package com.mvc.cryptovault.app.controller;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 广告相关
 *
 * @author qiyichen
 * @create 2018/11/5 16:13
 */
public class AdController extends BaseController {

    @ApiOperation("获取首屏广告图片")
    @GetMapping("screen")
    @SwaggerMock("${ad.screen}")
    @HystrixCommand(fallbackMethod = "findByIdFallback")
    public Result getScreen(HttpServletResponse response) throws IOException, InterruptedException {
        Result result = new Result(500, "服务器错误", null);
        response.setContentType("application/json;charset=UTF-8");
        @Cleanup PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(result));
        return null;
    }


    public Result findByIdFallback(HttpServletResponse response) {
        Result result = new Result(500, "服务器超时", null);
        return result;
    }

}
