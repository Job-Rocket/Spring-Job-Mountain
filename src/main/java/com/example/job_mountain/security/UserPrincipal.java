package com.example.job_mountain.security;

import com.example.job_mountain.user.domain.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails {
    private Long userId;
    private String id;
    private String pw;
    private String userName;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(Long userId, String id, String pw, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.id = id;
        this.pw = pw;
        this.authorities = authorities;
    }

    public static UserPrincipal create(SiteUser user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user.getUserId(),
                user.getId(),
                user.getPw(),
                authorities);
    }

    public static UserPrincipal create(SiteUser user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Long getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() { return userName; }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return null;
    }

//    @Override
//    public String getName() {
//        return String.valueOf(id);
//    }
}
