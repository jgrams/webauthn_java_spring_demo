package com.webauthn.app.utility;

import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.AssertionRequest;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.stereotype.Service;

@Service
public class EhCache {
    private CacheManager cacheManager;
    private Cache<ByteArray, PublicKeyCredentialCreationOptions> credentialCache;
    private Cache<String, AssertionRequest> requestCache;

    public EhCache() {
        this.cacheManager = CacheManagerBuilder
            .newCacheManagerBuilder()
            .build(true);
        this.credentialCache = cacheManager.createCache("credentialCache",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(ByteArray.class, PublicKeyCredentialCreationOptions.class,
            ResourcePoolsBuilder.heap(10)).build());
        this.requestCache = cacheManager.createCache("requestCache",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, AssertionRequest.class,
            ResourcePoolsBuilder.heap(10)).build());
    };

    public Cache<ByteArray, PublicKeyCredentialCreationOptions> getCredentialCache() {
        return this.credentialCache;
    }

    public Cache<String, AssertionRequest> getRequestCache() {
        return this.requestCache;
    }
}
