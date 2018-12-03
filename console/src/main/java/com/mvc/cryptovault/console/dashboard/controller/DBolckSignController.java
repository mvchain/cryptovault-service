package com.mvc.cryptovault.console.dashboard.controller;

import com.mvc.cryptovault.common.bean.BlockSign;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.BlockSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/21 16:42
 */
@RestController
@RequestMapping("dashboard/blockSign")
public class DBolckSignController extends BaseController {
    @Autowired
    BlockSignService blockSignService;

    @PostMapping()
    Result<Boolean> importSign(@RequestBody List<BlockSign> list, @RequestParam("fileName") String fileName) {

        String obj = redisTemplate.boundValueOps(RedisConstant.TRANS_IMPORT + fileName).get();
        if (null != obj) {
            throw new IllegalArgumentException("该文件正在导入,请稍后");
        }
        blockSignService.importSign(list, fileName);
        return new Result<>(true);
    }

}
