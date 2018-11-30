package com.mvc.cryptovault.console.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qyc
 */
@Data
@ConfigurationProperties
@Component
public class NodeConfiguration {

    private String nodeEndpoint;
    private String fromAddress;
}
