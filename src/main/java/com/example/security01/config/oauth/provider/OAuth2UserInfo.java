package com.example.security01.config.oauth.provider;

public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

}
