package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.bean.vo.MessageVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 消息相关
 *
 * @author qiyichen
 * @create 2018/11/8 14:23
 */
@RestController
@Api("消息相关")
@RequestMapping("message")
public class MessageController extends BaseController {

    @ApiOperation("传入时间戳获取通知信息,本地需要保存已读状态,服务器已读状态变更优先度很低")
    @GetMapping
    @SwaggerMock("${message.list}")
    public Result<MessageVO> getlist(@RequestParam(required = false) String timestamp) {
        return mockResult;
    }
    @ApiOperation("变更已读状态")
    @PutMapping("{id}")
    @SwaggerMock("${message.read}")
    public Result<Boolean> read(){
        return mockResult;
    }



}
