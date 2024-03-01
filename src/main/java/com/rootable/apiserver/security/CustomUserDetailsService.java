package com.rootable.apiserver.security;

import com.rootable.apiserver.domain.Member;
import com.rootable.apiserver.dto.MemberDTO;
import com.rootable.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /*
    * UserDetails == MemberDTO (return type)
    * username == email (primary value)
    * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("----------------loadUserByUsername-----------------------------" + username);

        Member member = memberRepository.getWithRoles(username);

        if (member == null) throw new UsernameNotFoundException("Not found the Member");

        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList()
                        .stream()
                        .map(Enum::name).collect(Collectors.toList()) //Enum -> String -> List<String>
        );

        log.info(memberDTO);

        return memberDTO;

    }

}
