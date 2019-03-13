package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.ExplorerBlockUser;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.ExplorerBlockUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qiyichen
 * @create 2019/3/12 13:17
 */
@Service
public class ExplorerBlockUserService extends AbstractService<ExplorerBlockUser> implements BaseService<ExplorerBlockUser> {

    @Autowired
    ExplorerBlockUserMapper explorerBlockUserMapper;


    public ExplorerBlockUser getLast() {
        PageHelper.startPage(1, 1, "id desc");
        List<ExplorerBlockUser> result = explorerBlockUserMapper.selectAll();
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    public void saveInfo(ExplorerBlockUser user) {
        int num = explorerBlockUserMapper.insert(user);
    }
}
