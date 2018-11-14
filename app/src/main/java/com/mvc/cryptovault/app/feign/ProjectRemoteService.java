package com.mvc.cryptovault.app.feign;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.dto.ProjectBuyDTO;
import com.mvc.cryptovault.common.bean.dto.ProjectDTO;
import com.mvc.cryptovault.common.bean.dto.ReservationDTO;
import com.mvc.cryptovault.common.bean.vo.ProjectBuyVO;
import com.mvc.cryptovault.common.bean.vo.PurchaseVO;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@FeignClient("console")
public interface ProjectRemoteService {

    @GetMapping("appProject")
    Result<PageInfo<AppProject>> getProject(@ModelAttribute ProjectDTO projectDTO);

    @GetMapping("appProject/{id}")
    Result<AppProject> getProjectById(@PathVariable BigInteger id);

    @GetMapping("appProjectUserTransaction")
    Result<PageInfo<PurchaseVO>> getReservation(@RequestParam("userId") BigInteger userId, @ModelAttribute ReservationDTO reservationDTO);

    @GetMapping("appProjectUserTransaction/chaseInfo")
    Result<ProjectBuyVO> getPurchaseInfo(@RequestParam("userId") BigInteger userId, @RequestParam("projectId") BigInteger id);

    @PostMapping("appProjectUserTransaction/buy")
    Result<Boolean> buy(@RequestParam("userId") BigInteger userId, @RequestParam("projectId") BigInteger id, @ModelAttribute ProjectBuyDTO dto);

}
