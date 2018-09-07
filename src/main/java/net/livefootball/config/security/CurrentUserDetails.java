package net.livefootball.config.security;

import lombok.Getter;
import net.livefootball.model.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUserDetails extends org.springframework.security.core.userdetails.User{

    @Getter
    private User user;

    public CurrentUserDetails(User user) {
        super(user.getUsername(),user.getPassword(),AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }
}