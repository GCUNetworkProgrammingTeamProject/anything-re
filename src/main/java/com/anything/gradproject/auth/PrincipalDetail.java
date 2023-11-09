package com.anything.gradproject.auth;

import com.anything.gradproject.entity.Member;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class PrincipalDetail implements UserDetails {
    private Member member;

    public PrincipalDetail(Member member) {
        this.member = member;
    }



    @Override
    public String getPassword() { return member.getPassword(); }
    @Override
    public String getUsername() {
        return member.getId();
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

    // 계정이 갖고있는 권한 목록을 리턴한다. (권한이 여러개 있을땐 루프돌려야 함)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collectors = new ArrayList<>();

        collectors.add(() -> {return "ROLE_" + member.getRole();});

        return collectors;
    }
}
