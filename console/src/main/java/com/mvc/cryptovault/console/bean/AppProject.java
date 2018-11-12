package com.mvc.cryptovault.console.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

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
     * 项目图片
     */
    @Column(name = "project_image")
    private String projectImage;

    /**
     * 
     */
    @Column(name = "created_at")
    private BigInteger createdAt;

    /**
     * 
     */
    @Column(name = "updated_at")
    private BigInteger updatedAt;

    /**
     * 是否展示
     */
    @Column(name = "visiable")
    private Integer visiable;

    /**
     * 开始时间
     */
    @Column(name = "started_at")
    private BigInteger startedAt;

    /**
     * 结束时间
     */
    @Column(name = "sopt_at")
    private BigInteger soptAt;

    /**
     * 发币时间
     */
    @Column(name = "publish_at")
    private BigInteger publishAt;

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
    private Double projectLimit;

    /**
     * app_project
     */
    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     * @return id 项目id
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * 项目id
     * @param id 项目id
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     * 项目名称
     * @return project_name 项目名称
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * 项目名称
     * @param projectName 项目名称
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 基础币种id
     * @return base_token_id 基础币种id
     */
    public BigInteger getBaseTokenId() {
        return baseTokenId;
    }

    /**
     * 基础币种id
     * @param baseTokenId 基础币种id
     */
    public void setBaseTokenId(BigInteger baseTokenId) {
        this.baseTokenId = baseTokenId;
    }

    /**
     * 项目图片
     * @return project_image 项目图片
     */
    public String getProjectImage() {
        return projectImage;
    }

    /**
     * 项目图片
     * @param projectImage 项目图片
     */
    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    /**
     * 
     * @return created_at 
     */
    public BigInteger getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt 
     */
    public void setCreatedAt(BigInteger createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return updated_at 
     */
    public BigInteger getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     * @param updatedAt 
     */
    public void setUpdatedAt(BigInteger updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 是否展示
     * @return visiable 是否展示
     */
    public Integer getVisiable() {
        return visiable;
    }

    /**
     * 是否展示
     * @param visiable 是否展示
     */
    public void setVisiable(Integer visiable) {
        this.visiable = visiable;
    }

    /**
     * 开始时间
     * @return started_at 开始时间
     */
    public BigInteger getStartedAt() {
        return startedAt;
    }

    /**
     * 开始时间
     * @param startedAt 开始时间
     */
    public void setStartedAt(BigInteger startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * 结束时间
     * @return sopt_at 结束时间
     */
    public BigInteger getSoptAt() {
        return soptAt;
    }

    /**
     * 结束时间
     * @param soptAt 结束时间
     */
    public void setSoptAt(BigInteger soptAt) {
        this.soptAt = soptAt;
    }

    /**
     * 发币时间
     * @return publish_at 发币时间
     */
    public BigInteger getPublishAt() {
        return publishAt;
    }

    /**
     * 发币时间
     * @param publishAt 发币时间
     */
    public void setPublishAt(BigInteger publishAt) {
        this.publishAt = publishAt;
    }

    /**
     * 众筹数量
     * @return project_total 众筹数量
     */
    public BigDecimal getProjectTotal() {
        return projectTotal;
    }

    /**
     * 众筹数量
     * @param projectTotal 众筹数量
     */
    public void setProjectTotal(BigDecimal projectTotal) {
        this.projectTotal = projectTotal;
    }

    /**
     * 兑换比例
     * @return ratio 兑换比例
     */
    public Float getRatio() {
        return ratio;
    }

    /**
     * 兑换比例
     * @param ratio 兑换比例
     */
    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    /**
     * 释放比例
     * @return release_value 释放比例
     */
    public Float getReleaseValue() {
        return releaseValue;
    }

    /**
     * 释放比例
     * @param releaseValue 释放比例
     */
    public void setReleaseValue(Float releaseValue) {
        this.releaseValue = releaseValue;
    }

    /**
     * 限购数量
     * @return project_limit 限购数量
     */
    public Double getProjectLimit() {
        return projectLimit;
    }

    /**
     * 限购数量
     * @param projectLimit 限购数量
     */
    public void setProjectLimit(Double projectLimit) {
        this.projectLimit = projectLimit;
    }
}