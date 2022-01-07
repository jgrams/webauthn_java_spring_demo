package com.webauthn.app;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "authn")
@Getter
@Setter
public class WebAuthProperties {

    private String hostName;
    private String display;
    private Set<String> origin;

}