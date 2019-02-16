package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.dto.ProjectBuyDTO;
import com.mvc.cryptovault.common.bean.dto.ProjectDTO;
import com.mvc.cryptovault.common.bean.dto.ReservationDTO;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
        return new Result<>(projectService.getProject(getUserId(), projectDTO));
    }

    @ApiOperation("获取参与的项目列表")
    @GetMapping("reservation")
    @SwaggerMock("${project.reservation}")
    public Result<List<PurchaseVO>> getReservation(@ModelAttribute @Valid ReservationDTO reservationDTO) {
        return new Result<>(projectService.getReservation(getUserId(), reservationDTO));
    }

    @ApiOperation("通过项目id获取项目购买信息")
    @GetMapping("{id}/purchase")
    @SwaggerMock("${project.purchase}")
    public Result<ProjectBuyVO> getPurchaseInfo(@PathVariable BigInteger id) {
        return new Result<>(projectService.getPurchaseInfo(getUserId(), id));
    }

    @ApiOperation("购买项目")
    @PostMapping("{id}/purchase")
    @SwaggerMock("${project.buy}")
    public Result<Boolean> buy(@PathVariable BigInteger id, @RequestBody @Valid ProjectBuyDTO dto) {
        return new Result<>(projectService.buy(getUserId(), id, dto));
    }

    @ApiOperation("已发币项目列表")
    @GetMapping("publish")
    public Result<List<ProjectPublishVO>> getPublish(@RequestParam(value = "projectId", required = false) @ApiParam("上一条记录id,不传或0时则从头拉取") BigInteger projectId, @ModelAttribute PageDTO pageDTO) {
        return new Result<>(projectService.getPublish(getUserId(), projectId, pageDTO));
    }

    @ApiOperation("参与项目详情")
    @GetMapping("{projectId}/publish")
    public Result<ProjectPublishDetailVO> getPublishDetail(@PathVariable BigInteger projectId) {
        return new Result<>(projectService.getPublishDetail(getUserId(), projectId));
    }

    @ApiOperation("发币记录列表")
    @GetMapping("{projectId}/publish/list")
    public Result<List<ProjectPublishListVO>> getPublishList(@PathVariable BigInteger projectId, @RequestParam(value = "id", required = false) @ApiParam("上一条记录id,不传或0时则从头拉取") BigInteger id, @ModelAttribute PageDTO pageDTO) {
        return new Result<>(projectService.getPublishList(getUserId(), projectId, id, pageDTO));
    }

}
