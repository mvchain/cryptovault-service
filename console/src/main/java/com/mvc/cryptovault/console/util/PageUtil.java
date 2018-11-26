package com.mvc.cryptovault.console.util;

import com.mvc.cryptovault.common.bean.dto.PageDTO;

import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/26 15:50
 */
public class PageUtil {

    public static List subList(List list, PageDTO pageDTO) {
        Integer start = 0;
        Integer end = list.size();
        if (null != pageDTO.getPageSize() && null != pageDTO.getPageNum()) {
            if (pageDTO.getPageNum() < 1) {
                pageDTO.setPageNum(1);
            }
            start = (pageDTO.getPageNum() - 1) * pageDTO.getPageSize();
            end = start + pageDTO.getPageSize();
            if (start > list.size()) {
                start = list.size();
            }
            if (end > list.size()) {
                end = list.size();
            }
        }
        return list.subList(start, end);
    }
}
