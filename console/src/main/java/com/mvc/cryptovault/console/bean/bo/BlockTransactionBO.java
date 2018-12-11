package com.mvc.cryptovault.console.bean.bo;

import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/11 14:56
 */
@Data
public class BlockTransactionBO {

    private BigInteger userId;
    private TransactionDTO transactionDTO;

}
