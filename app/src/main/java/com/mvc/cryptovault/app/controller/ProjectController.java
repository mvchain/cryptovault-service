package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.bean.dto.ProjectDTO;
import com.mvc.cryptovault.app.bean.dto.ReservationDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;

/**
 * 项目相关
 *
 * @author qiyichen
 * @create 2018/11/7 13:47
 */
@Api(tags = "项目相关")
@RequestMapping("project")
@RestController
public class ProjectController extends BaseController {

    @ApiOperation("获取项目列表")
    @GetMapping
    @SwaggerMock("${project.all}")
    public Result getProject(@ModelAttribute @Valid ProjectDTO projectDTO) {
        return mockResult;
    }

    @ApiOperation("获取预约列表")
    @GetMapping("reservation")
    @SwaggerMock("${project.reservation}")
    public Result getReservation(@ModelAttribute @Valid ReservationDTO reservationDTO) {
        return mockResult;
    }

    @ApiOperation("通过项目id获取项目购买信息")
    @GetMapping("{id}/purchase")
    @SwaggerMock("${project.purchase}")
    public Result getPurchaseInfo(@PathVariable BigInteger id) {
        return mockResult;
    }

    @ApiOperation("购买项目")
    @PostMapping("{id}/purchase")
    @SwaggerMock("${project.buy}")
    public Result buy(@PathVariable BigInteger id) {
        return mockResult;
    }

}
