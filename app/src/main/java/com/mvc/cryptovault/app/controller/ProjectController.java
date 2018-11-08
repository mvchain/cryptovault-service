package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.bean.dto.ProjectBuyDTO;
import com.mvc.cryptovault.app.bean.dto.ProjectDTO;
import com.mvc.cryptovault.app.bean.dto.ReservationDTO;
import com.mvc.cryptovault.app.bean.vo.ProjectBuyVO;
import com.mvc.cryptovault.app.bean.vo.ProjectSimpleVO;
import com.mvc.cryptovault.app.bean.vo.PurchaseVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

/**
 * 项目相关
 *
 * @author qiyichen
 * @create 2018/11/7 13:47
 */
@Api(tags = "项目众筹相关")
@RequestMapping("project")
@RestController
public class ProjectController extends BaseController {

    @ApiOperation("获取项目列表")
    @GetMapping
    @SwaggerMock("${project.all}")
    public Result<List<ProjectSimpleVO>> getProject(@ModelAttribute @Valid ProjectDTO projectDTO) {
        return mockResult;
    }

    @ApiOperation("获取参与的项目列表")
    @GetMapping("reservation")
    @SwaggerMock("${project.reservation}")
    public Result<PurchaseVO> getReservation(@ModelAttribute @Valid ReservationDTO reservationDTO) {
        return mockResult;
    }

    @ApiOperation("通过项目id获取项目购买信息")
    @GetMapping("{id}/purchase")
    @SwaggerMock("${project.purchase}")
    public Result<ProjectBuyVO> getPurchaseInfo(@PathVariable BigInteger id) {
        return mockResult;
    }

    @ApiOperation("购买项目")
    @PostMapping("{id}/purchase")
    @SwaggerMock("${project.buy}")
    public Result<Boolean> buy(@PathVariable BigInteger id, @RequestBody @Valid ProjectBuyDTO dto) {
        return mockResult;
    }

}
