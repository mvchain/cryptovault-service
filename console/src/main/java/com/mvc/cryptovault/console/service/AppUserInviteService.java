package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.AppUserInvite;
import com.mvc.cryptovault.common.bean.dto.RecommendDTO;
import com.mvc.cryptovault.common.bean.vo.RecommendVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppUserInviteMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserInviteService extends AbstractService<AppUserInvite> implements BaseService<AppUserInvite> {

    @Autowired
    AppUserInviteMapper appUserMapper;
    @Autowired
    AppUserService appUserService;

    public List<RecommendVO> getRecommend(RecommendDTO dto) {
        String str = "";
        if (dto.getInviteUserId() != null && !dto.getInviteUserId().equals(BigInteger.ZERO)) {
            str = " and invite_user_id < " + dto.getInviteUserId();
        }
        String ids = appUserMapper.getRecommend(dto, str);
        if (StringUtils.isBlank(ids)) {
            return null;
        }
        Condition condition = new Condition(AppUser.class);
        Example.Criteria criteria = condition.createCriteria();
        PageHelper.orderBy("id desc");
        ConditionUtil.andCondition(criteria, "id in (" + ids + ")");
        List<AppUser> list = appUserService.findByCondition(condition);
        List<RecommendVO> result = list.stream().map(obj -> {
            RecommendVO vo = new RecommendVO();
            vo.setUserId(obj.getId());
            vo.setEmail(obj.getEmail());
            vo.setNickname(obj.getNickname());
            vo.setCreatedAt(obj.getCreatedAt());
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    public void insert(BigInteger userId, BigInteger inviteUserId) {
        AppUserInvite invite = new AppUserInvite();
        invite.setUserId(userId);
        invite.setInviteUserId(inviteUserId);
        AppUserInvite temp = findOneByEntity(invite);
        if (null == temp) {
            Long time = System.currentTimeMillis();
            invite.setCreatedAt(time);
            invite.setUpdatedAt(time);
            save(invite);
        }
    }
}