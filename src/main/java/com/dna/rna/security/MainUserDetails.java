package com.dna.rna.security;

import com.dna.rna.BeanUtils;
import com.dna.rna.domain.User.User;
import com.dna.rna.service.SigninService;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.transaction.Transactional;
import java.util.Collection;

@NoArgsConstructor
public class MainUserDetails implements UserDetails {

    private User user;
    private SigninService signinService;

    public MainUserDetails(User user) {
        this.user = user;
        this.signinService = (SigninService)BeanUtils.getBean("SinginService");
    }

    @Transactional
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return signinService.getAuthorities(user.getLoginId());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

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
}
