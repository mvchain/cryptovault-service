package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.TokenVolume;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Select;

public interface TokenVolumeMapper extends MyMapper<TokenVolume> {

    @Select("select * from token_volume where used = 0 and token_id > 4 order by id asc limit 1")
    TokenVolume getNext();
}