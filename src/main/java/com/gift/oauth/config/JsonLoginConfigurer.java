package com.gift.oauth.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

import com.gift.filter.JwtLoginfilter;
import com.gift.oauth.exception.MyAuthenticationFailureHandler;
import com.gift.oauth.exception.MyAuthenticationSuccessHandler;

public class JsonLoginConfigurer<T extends JsonLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B>  {

    private JwtLoginfilter authFilter;

    public JsonLoginConfigurer(AuthenticationManager authenticationManager) {
        this.authFilter = new JwtLoginfilter(authenticationManager);
    }
    
    @Override
    public void configure(B http) throws Exception {
        //设置Filter使用的AuthenticationManager,这里取公共的即可
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //设置失败的Handler
        authFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        //设置成功的Handler
        authFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        //不将认证后的context放入session
        authFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

        JwtLoginfilter filter = postProcess(authFilter);
        //指定Filter的位置
        http.addFilterAfter(filter, LogoutFilter.class);
    }
    //设置成功的Handler，这个handler定义成Bean，所以从外面set进来
    public JsonLoginConfigurer<T,B> loginSuccessHandler(AuthenticationSuccessHandler authSuccessHandler){
        authFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        return this;
    }

}
