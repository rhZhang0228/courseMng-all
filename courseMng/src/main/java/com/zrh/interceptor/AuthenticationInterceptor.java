package com.zrh.interceptor;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zrh.dto.User;
import com.zrh.service.UserService;
import com.zrh.utils.PassToken;
import com.zrh.utils.TokenConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * 认证相关
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.value()) {
                //加注解直接放行
                return true;
            }
        }

        String token = request.getHeader(TokenConstant.TOKEN);
        String refreshToken = request.getHeader(TokenConstant.REFRESH_TOKEN);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(refreshToken)) {
            log.error("jwt为空");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return false;
        }
        List<String> audienceList = JWT.decode(token).getAudience();
        if (CollectionUtils.isEmpty(audienceList)) {
            log.debug("用户信息错误，jwt的信息为空，请重新登录");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return false;
        }
        String userInfo = audienceList.get(0);
        String level = userInfo.substring(0, 1);
        String id = userInfo.substring(1);
        User user = userService.getUserByLevelAndId(level, id);
        if (ObjectUtils.isEmpty(user)) {
            log.error("用户不存在，请重新登录");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "用户不存在，请重新登录");
            return false;
        }
        Date expires = JWT.decode(token).getExpiresAt();
        Date now = new Date();
        log.debug("旧的token为：{}", token);
        //token过期了
        if (expires.before(now)) {
            Date refreshTokenExpires = JWT.decode(refreshToken).getExpiresAt();
            //refreshtoken也过期
            if (refreshTokenExpires.before(now)) {
                log.error("token和refreshToken都已过期");
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            //检验refreshToken
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            try {
                jwtVerifier.verify(refreshToken);
            } catch (JWTVerificationException e) {
                log.error("refreshToken的jwt验证失败");
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            //刷新token
            token = userService.getToken(user);
            refreshToken = userService.getRefreshToken(user);
            log.debug("token过期，利用refreshToken刷新了token");
            log.debug("过期时间为:{}", expires);
        } else {
            //token没哟过期，检验token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            try {
                jwtVerifier.verify(token);
            } catch (JWTVerificationException e) {
                log.error("jwt验证失败");
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            long minute = DateUtil.between(expires, now, DateUnit.MINUTE);
            //小于五分钟，刷新token
            if (minute <= 5) {
                token = userService.getToken(user);
                refreshToken = userService.getRefreshToken(user);
                log.debug("刷新了token即将过期，刷新了token");
            }
        }
        log.debug("新的token-->{}", token);
        log.debug("时间为：--->{}", JWT.decode(token).getExpiresAt());
        response.setHeader(TokenConstant.TOKEN, token);
        response.setHeader(TokenConstant.REFRESH_TOKEN, refreshToken);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    public static void main(String[] args) {
        System.out.println(HttpMethod.OPTIONS);
        System.out.println(HttpMethod.OPTIONS.name().equalsIgnoreCase("options"));
    }
}
