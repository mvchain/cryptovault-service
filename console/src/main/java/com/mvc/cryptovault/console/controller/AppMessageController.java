package com.mvc.cryptovault.console.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppMessage;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.dto.TimeSearchDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.AppMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/13 16:02
 */
@RestController
@RequestMapping("appMessage")
public class AppMessageController extends BaseController {

    @Autowired
    AppMessageService appMessageService;

    /**
     * TODO 根据性能结果缓存或走搜索引擎
     *
     * @param userId
     * @param timeSearchDTO
     * @param pageDTO
     * @return
     */
    @GetMapping()
    public Result<PageInfo<AppMessage>> list(@RequestParam BigInteger userId, @ModelAttribute TimeSearchDTO timeSearchDTO, @ModelAttribute PageDTO pageDTO) {
        List<AppMessage> message = appMessageService.list(userId, timeSearchDTO, pageDTO);
        return new Result<>(new PageInfo<>(message));
    }

    /**
     * TODO 添加到低优先级消息队列
     *
     * @param userId
     * @param id
     * @return
     */
    @PutMapping("{id}")
    public Result<Boolean> updateRead(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id) {
        AppMessage message = new AppMessage();
        message.setUserId(userId);
        message.setId(id);
        message.setIsRead(1);
        appMessageService.update(message);
        return new Result<>(true);
    }
}
