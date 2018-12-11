package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.TokenVolume;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.TokenVolumeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenVolumeService extends AbstractService<TokenVolume> implements BaseService<TokenVolume> {

    @Autowired
    TokenVolumeMapper tokenVolumeMapper;

    public TokenVolume getNext() {
        return tokenVolumeMapper.getNext();
    }
}