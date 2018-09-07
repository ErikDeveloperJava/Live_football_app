package net.livefootball.interceptor;

import net.livefootball.config.security.CurrentUserDetails;
import net.livefootball.model.User;
import net.livefootball.model.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user;
        if(authentication.getPrincipal() instanceof CurrentUserDetails){
            CurrentUserDetails currentUserDetails = (CurrentUserDetails) authentication.getPrincipal();
            user = currentUserDetails.getUser();
        }else {
            user = User.builder()
                    .role(UserRole.ROLE_ANONYMOUS)
                    .build();
        }
        request.setAttribute("user",user);
        return true;
    }
}