package com.mvc.cryptovault.console.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.CommonTokenPrice;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author qiyichen
 * @create 2018/11/13 15:34
 */
@RequestMapping("commonTokenPrice")
public class CommonTokenPriceController extends BaseController {

    @GetMapping
    public Result<PageInfo<CommonTokenPrice>> getAll(PageDTO pageDTO) {
        return new Result<>(new PageInfo<>(commonTokenPriceService.findAll()));
    }

}
