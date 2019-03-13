package com.mvc.cryptovault.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/12 13:38
 */
@Data
@ApiModel("区块链浏览器设置")
public class ExplorerBlockSetting {

    @Id
    @ApiModelProperty("记录id,只有1条")
    private BigInteger id;
    @ApiModelProperty("最小交易笔数，10以上")
    private Integer minTransaction;
    @ApiModelProperty("最大交易笔数，100一下")
    private Integer maxTransaction;
    @ApiModelProperty("最小难度")
    private Integer minDifficult;
    @ApiModelProperty("最大难度")
    private Integer maxDifficult;
    @ApiModelProperty("有效开始区块")
    private BigInteger startBlock;
    @ApiModelProperty("当前版本")
    private String version;
    @ApiModelProperty("发行总量")
    private BigInteger total;
    @ApiModelProperty("当前交易笔数,无需设置")
    private BigInteger totalTransaction;

    public Integer getRandomTransactionCount() {
        if (null == minTransaction || null == maxTransaction) {
            return 0;
        }
        return minTransaction + (int) (Math.random() * (maxTransaction - minTransaction) + 1);
    }

    public Integer getRandomDifficult() {
        if (null == minDifficult || null == maxDifficult) {
            return 0;
        }
        return minDifficult + (int) (Math.random() * (maxDifficult - minDifficult) + 1);
    }
}
