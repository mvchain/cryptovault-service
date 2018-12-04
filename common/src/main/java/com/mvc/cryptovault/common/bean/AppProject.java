package com.mvc.cryptovault.common.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import lombok.Data;
import lombok.Generated;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * app_project
 */
@Table(name = "app_project")
@Data
public class AppProject implements Serializable {
    /**
     * 项目id
     */
    @Id
    @Column(name = "id")
    private BigInteger id;

    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 基础币种id
     */
    @Column(name = "base_token_id")
    private BigInteger baseTokenId;

    /**
     * 基础币种名称
     */
    @Column(name = "base_token_name")
    private String baseTokenName;

    /**
     * 币种id
     */
    @Column(name = "token_id")
    private BigInteger tokenId;

    /**
     * 币种名称
     */
    @Column(name = "token_name")
    private String tokenName;

    /**
     * 项目图片
     */
    @Column(name = "project_image")
    private String projectImage;

    /**
     * 交易对ID
     */
    @Column(name = "pair_id")
    private BigInteger pairId;
    /**
     *
     */
    @Column(name = "created_at")
    private BigInteger createdAt;
    /**
     * 项目状态0即将开始 1进行中 2已结束 3发币中 9取消
     */
    @Column(name = "status")
    private Integer status;

    /**
     *
     */
    @Column(name = "updated_at")
    private Long updatedAt;

    /**
     * 是否展示
     */
    @Column(name = "visiable")
    private Integer visiable;

    /**
     * 开始时间
     */
    @Column(name = "started_at")
    private Long startedAt;

    /**
     * 结束时间
     */
    @Column(name = "stop_at")
    private Long stopAt;

    /**
     * 发币时间
     */
    @Column(name = "publish_at")
    private Long publishAt;

    /**
     * 众筹数量
     */
    @Column(name = "project_total")
    private BigDecimal projectTotal;

    /**
     * 兑换比例
     */
    @Column(name = "ratio")
    private Float ratio;

    /**
     * 释放比例
     */
    @Column(name = "release_value")
    private Float releaseValue;

    /**
     * 限购数量
     */
    @Column(name = "project_limit")
    private BigDecimal projectLimit;

    /**
     * 最小购买数量
     */
    @Column(name = "project_min")
    private BigDecimal projectMin;
    /**
     * app_project
     */
    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     *
     * @return id 项目id
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * 项目id
     *
     * @param id 项目id
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     * 项目名称
     *
     * @return project_name 项目名称
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * 项目名称
     *
     * @param projectName 项目名称
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 基础币种id
     *
     * @return base_token_id 基础币种id
     */
    public BigInteger getBaseTokenId() {
        return baseTokenId;
    }

    /**
     * 基础币种id
     *
     * @param baseTokenId 基础币种id
     */
    public void setBaseTokenId(BigInteger baseTokenId) {
        this.baseTokenId = baseTokenId;
    }

    /**
     * 项目图片
     *
     * @return project_image 项目图片
     */
    public String getProjectImage() {
        return projectImage;
    }

    /**
     * 项目图片
     *
     * @param projectImage 项目图片
     */
    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    /**
     * @return created_at
     */
    public BigInteger getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt
     */
    public void setCreatedAt(BigInteger createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 是否展示
     *
     * @return visiable 是否展示
     */
    public Integer getVisiable() {
        return visiable;
    }

    /**
     * 是否展示
     *
     * @param visiable 是否展示
     */
    public void setVisiable(Integer visiable) {
        this.visiable = visiable;
    }

    /**
     * 众筹数量
     *
     * @return project_total 众筹数量
     */
    public BigDecimal getProjectTotal() {
        return projectTotal;
    }

    /**
     * 众筹数量
     *
     * @param projectTotal 众筹数量
     */
    public void setProjectTotal(BigDecimal projectTotal) {
        this.projectTotal = projectTotal;
    }

    /**
     * 兑换比例
     *
     * @return ratio 兑换比例
     */
    public Float getRatio() {
        return ratio;
    }

    /**
     * 兑换比例
     *
     * @param ratio 兑换比例
     */
    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    /**
     * 释放比例
     *
     * @return release_value 释放比例
     */
    public Float getReleaseValue() {
        return releaseValue;
    }

    /**
     * 释放比例
     *
     * @param releaseValue 释放比例
     */
    public void setReleaseValue(Float releaseValue) {
        this.releaseValue = releaseValue;
    }

}