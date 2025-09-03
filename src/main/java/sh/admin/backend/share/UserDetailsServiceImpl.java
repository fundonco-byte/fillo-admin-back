package sh.admin.backend.share;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sh.admin.backend.member.domain.Member;
import sh.admin.backend.member.repository.MemberRepository;


import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Value("${admin.account}")
    private String adminLoginId;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        if(adminLoginId.equals(loginId)){
            return createUserDetailsOfAdmin();
        }else{
            log.info("인증된 관리자 계정 없음");
            return null;
        }
    }

    private UserDetails createUserDetailsOfAdmin() {
        log.info("관리자용 createUserDetails 실행");

        return User.builder()
                .username(adminLoginId)
                .password(passwordEncoder.encode(adminPassword))
                .roles("admin")
                .build();
    }
}