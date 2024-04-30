package com.example.job_mountain.security;

import com.example.job_mountain.company.domain.Company;
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
public class CompanyPrincipal implements OAuth2User, UserDetails {
    private Long companyId;
    private String id;
    private String pw;
    private String companyName;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CompanyPrincipal(Long companyId, String id, String pw, Collection<? extends GrantedAuthority> authorities) {
        this.companyId = companyId;
        this.id = id;
        this.pw = pw;
        this.authorities = authorities;
    }

    public static CompanyPrincipal create(Company company) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new CompanyPrincipal(
                company.getCompanyId(),
                company.getId(),
                company.getPw(),
                authorities);
    }

    public static CompanyPrincipal create(Company company, Map<String, Object> attributes) {
        CompanyPrincipal userPrincipal = CompanyPrincipal.create(company);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() { return companyName; }

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
}
