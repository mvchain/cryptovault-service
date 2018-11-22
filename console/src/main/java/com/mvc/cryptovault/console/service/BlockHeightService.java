package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.BlockHeight;
import com.mvc.cryptovault.common.dashboard.bean.vo.DHoldVO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.BlockHeightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlockHeightService extends AbstractService<BlockHeight> implements BaseService<BlockHeight> {
    @Autowired
    BlockHeightMapper blockHeightMapper;

    public void setHold(List<DHoldVO> list) {
        for (DHoldVO vo : list) {
            blockHeightMapper.updateHold(vo.getTokenId(), vo.getValue());
        }
        updateAllCache();
    }

    public void setFee(List<DHoldVO> list) {
        for (DHoldVO vo : list) {
            blockHeightMapper.updateFee(vo.getTokenId(), vo.getValue());
        }
        updateAllCache();
    }
}