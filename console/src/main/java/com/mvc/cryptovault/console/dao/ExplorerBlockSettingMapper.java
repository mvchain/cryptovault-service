package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.ExplorerBlockSetting;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ExplorerBlockSettingMapper extends MyMapper<ExplorerBlockSetting> {

    @Update("update explorer_block_setting set total_transaction = total_transaction + #{transactions}")
    int updateValue(@Param("transactions") Integer transactions);
}