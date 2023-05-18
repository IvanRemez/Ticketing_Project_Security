package com.cydeo.entity.common;

import com.cydeo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
// Mapper class used to map User Entity fields unto Security User obj. used by Spring
// UserDetails object required by Spring Security
    // this implementation comes with specific methods which need to be overridden.

    private User user;  // Entity User

    public UserPrincipal(User user) {
        this.user = user;
    }
    // ^^ Constructor needed to pass Entity User (DB) in SecurityServiceImpl
    // loadUserByUsername() method needs to return UserDetails obj.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
                    // Interface ^^ to help Spring understand the Roles
        List<GrantedAuthority> authorityList = new ArrayList<>();

        GrantedAuthority authority = new SimpleGrantedAuthority(
                            // Implementation ^^ of the Interface
                this.user.getRole().getDescription());
                            // Role ^^ of the User (Entity) provided

        authorityList.add(authority);

        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.user.getPassWord();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
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
        return this.user.isEnabled();
    }
}
