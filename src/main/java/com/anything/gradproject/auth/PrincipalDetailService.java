package com.anything.gradproject.auth;

import com.anything.gradproject.entity.Member;
import com.anything.gradproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {


    private final MemberRepository memberRepository;

    //스프링이 로그인 요청을 가로챌 때 username, password 변수 2개를 가로채는데
    //usename이 db에 있는지만 확인하면 됨.
    //이 함수에서 확인
    @Override
    public UserDetails loadUserByUsername(String MemberId) throws UsernameNotFoundException {

        Member principal = memberRepository.findById(MemberId)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + MemberId);
                    });

        return new PrincipalDetail(principal); // 시큐리티의 세션에 유저 정보가 저장됨
    }
}
