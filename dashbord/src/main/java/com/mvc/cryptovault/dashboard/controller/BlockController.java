package com.mvc.cryptovault.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.BlockSign;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.ExportOrders;
import com.mvc.cryptovault.common.bean.OrderEntity;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockStatusDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockeTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DBlockeTransactionVO;
import com.mvc.cryptovault.common.permission.NotLogin;
import com.mvc.cryptovault.dashboard.util.EncryptionUtil;
import com.mvc.cryptovault.dashboard.util.ExcelException;
import com.mvc.cryptovault.dashboard.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:51
 */
@RestController
@RequestMapping("block")
@Api(tags = "区块链相关操作")
public class BlockController extends BaseController {

    private static LinkedHashMap<String, String> blockTransactionMap = new LinkedHashMap<>();

    @ApiOperation("区块链交易查询")
    @GetMapping("transactions")
    public Result<PageInfo<DBlockeTransactionVO>> getTransactions(@ModelAttribute @Valid DBlockeTransactionDTO dBlockeTransactionDTO) {
        PageInfo<DBlockeTransactionVO> result = blockService.getTransactions(dBlockeTransactionDTO);
        return new Result<>(result);
    }

    @ApiOperation("区块链交易导出")
    @NotLogin
    @GetMapping("transactions/excel")
    public void getTransactionsExcel(HttpServletResponse response, @RequestParam String sign, @ModelAttribute @Valid DBlockeTransactionDTO dBlockeTransactionDTO) throws IOException, ExcelException {
        getUserIdBySign(sign);
        PageInfo<DBlockeTransactionVO> result = blockService.getTransactions(dBlockeTransactionDTO);
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("block_transaction_%s.xls", System.currentTimeMillis()));
        @Cleanup OutputStream os = response.getOutputStream();
        ExcelUtil.listToExcel(result.getList(), getBlockTransactionMap(), "BlockTransactionTable", os);
    }

    private LinkedHashMap<String, String> getBlockTransactionMap() {
        if (blockTransactionMap.isEmpty()) {
            blockTransactionMap.put("createdAt", "时间");
            blockTransactionMap.put("orderNumber", "单号");
            blockTransactionMap.put("cellphone", "用户手机号");
            blockTransactionMap.put("value", "提币金额");
            blockTransactionMap.put("toAddress", "目标地址");
            blockTransactionMap.put("hash", "交易哈希");
            blockTransactionMap.put("transactionStatusStr", "状态");
            blockTransactionMap.put("errorData", "错误详情");
        }
        return blockTransactionMap;
    }

    @ApiOperation("账户余额查看")
    @GetMapping("balance/{tokeId}")
    public Result<BigDecimal> getBalance(@PathVariable BigInteger tokenId) {
        BigDecimal result = blockService.getBalance(tokenId);
        return new Result<>(result);
    }


    @ApiOperation("批量操作(1同意 2拒绝)")
    @PutMapping("status")
    public Result<Boolean> updateStatus(DBlockStatusDTO dBlockStatusDTO) {
        Boolean result = blockService.updateStatus(dBlockStatusDTO);
        return new Result<>(result);
    }

    @ApiOperation("待签名数据导出")
    @GetMapping("transaction/export")
    @NotLogin
    public void exportSign(HttpServletResponse response, @RequestParam String sign) throws Exception {
        getUserIdBySign(sign);
        List<ExportOrders> list = transactionService.exportSign();
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("withdraw_%s.json", System.currentTimeMillis()));
        @Cleanup OutputStream os = response.getOutputStream();
        @Cleanup BufferedOutputStream buff = new BufferedOutputStream(os);
        String jsonStr = JSON.toJSONString(list);
        String sig = EncryptionUtil.md5(("wallet-shell" + EncryptionUtil.md5(jsonStr)));
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSign(sig);
        orderEntity.setJsonStr(jsonStr);
        JSONObject object = new JSONObject();
        orderEntity.setExt(object);
        buff.write(JSON.toJSONBytes(orderEntity));
    }

    @ApiOperation("导入签名数据")
    @PostMapping("sign/import")
    public Result<Boolean> importSign(@RequestBody MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        @Cleanup InputStream in = file.getInputStream();
        String jsonStr = IOUtils.toString(in);
        List<BlockSign> list = null;
        try {
            list = JSON.parseArray(jsonStr, BlockSign.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("文件格式错误");
        }
        Assert.isTrue(null != list && list.size() > 0 && list.get(0).getOprType() != null, "文件格式错误");
        Boolean result = blockService.importSign(list, fileName);
        return new Result<>(true);
    }

    @ApiOperation("待汇总数据导出")
    @NotLogin
    @GetMapping("collect/export")
    public void exportCollect(HttpServletResponse response, @RequestParam String sign) throws Exception {
        getUserIdBySign(sign);
        List<ExportOrders> list = transactionService.exportCollect();
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("collect_%s.json", System.currentTimeMillis()));
        @Cleanup OutputStream os = response.getOutputStream();
        @Cleanup BufferedOutputStream buff = new BufferedOutputStream(os);
        String jsonStr = JSON.toJSONString(list);
        String sig = EncryptionUtil.md5(("wallet-shell" + EncryptionUtil.md5(jsonStr)));
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSign(sig);
        orderEntity.setJsonStr(jsonStr);
        JSONObject object = new JSONObject();
        orderEntity.setExt(object);
        buff.write(JSON.toJSONBytes(orderEntity));
    }

    @ApiOperation("导入账户")
    @PostMapping("account/import")
    public Result<Boolean> importAccount(@RequestBody MultipartFile file) throws IOException {
        @Cleanup InputStream in = file.getInputStream();
        String jsonStr = IOUtils.toString(in);
        List<CommonAddress> list = null;
        try {
            list = JSON.parseArray(jsonStr, CommonAddress.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("文件格式错误");
        }
        Assert.isTrue(null != list && list.size() > 0 && list.get(0).getAddress() != null, "文件格式错误");
        Boolean result = blockService.importAddress(list, file.getOriginalFilename());
        return new Result<>(true);
    }

    @ApiOperation("账户库存数据获取")
    @GetMapping("account/count")
    public Result<Integer> accountCount(@RequestParam String tokenType) {
        Integer result = blockService.accountCount(tokenType);
        return new Result<>(result);
    }

}