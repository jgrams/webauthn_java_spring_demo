package com.webauthn.app.configuration;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "authn")
@Getter
@Setter
public class WebAuthProperties {

    private String hostName;
    private String display;
    private Set<String> origin;

}