package project.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import project.entity.Login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class TokenInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    @Override
    @ResponseBody
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object o) throws Exception {

        Map<String, String> cookieMap = CookieUtil.getCookieMap(req.getCookies());
        if(null == cookieMap){
            logger.warn("cookieMap == null");
            res.setStatus(403);
            return false;
        }
        else{
            String loginId = cookieMap.get("loginId");
            String token = cookieMap.get("token");
            if(token == null){
                logger.warn("haven't token");
                res.setStatus(403);
                return false;
            }
            else if(loginId == null){
                logger.warn("haven't loginId");
                res.setStatus(403);
                return false;
            }
            else if(null == JWT.unsign(token, Login.class)){
                logger.warn("invalid JWT");
                res.setStatus(403);
                return false;
            }
            else if(loginId != JWT.unsign(token, Login.class).getId()){
                logger.warn("loginId is incorrect");
                res.setStatus(403);
                return false;
            }
            else{
                logger.info("jwt approved ! loginId: {}", loginId);
                return true;
            }
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
