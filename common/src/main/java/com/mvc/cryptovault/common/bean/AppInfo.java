package com.mvc.cryptovault.common.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * @author qiyichen
 * @create 2019/2/13 15:14
 */
@Data
public class AppInfo {

    @Id
    @ApiModelProperty("apk或ipa")
    private String appType;
    @ApiModelProperty("版本号")
    private String appVersion;
    @ApiModelProperty("内部版本号")
    private Integer appVersionCode;
    @ApiModelProperty("包名")
    private String appPackage;
    @ApiModelProperty("下载地址")
    private String httpUrl;

}