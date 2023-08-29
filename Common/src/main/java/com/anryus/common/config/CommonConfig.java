package com.anryus.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Getter
public class CommonConfig {
    @Value("${jks.password}")
    private String jksPassword;

    @Value("${jks.key-path}")
    private String jksKeyPath;

    @Value("${jks.alias}")
    private String jksAlias;
}
