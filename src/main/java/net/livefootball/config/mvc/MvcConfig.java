package net.livefootball.config.mvc;

import net.livefootball.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authenticationInterceptor)
                .excludePathPatterns("/resources/**","/register","/news/top/5",
                        "/error","/leagues","/news/comment/*/page/*","/admin/match/update")
                .addPathPatterns("/","/admin","/admin/league/add","/admin/club/add",
                        "/admin/player/add","/admin/news/add","/admin/league-table/add",
                        "/admin/match/add","/league/*","/clubs","/league/*/clubs","/club/*","/club/*/players",
                        "/matches/*","/news","/news/*","/news/comment","/admin/gallery","/gallery",
                        "/admin/user/add","/admin/clubs","/admin/club/update/*","/admin/club/update",
                        "/admin/league","/admin/league/update/*","/admin/matches","/admin/matches/*","/admin/news",
                        "/admin/news/update/*","/admin/news/update","/admin/players","/admin/player/update/*",
                        "/admin/player/update");
    }
}