package com.rootable.apiserver.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberRoleList")
public class Member {

    @Id
    private String email;

    private String pw;

    private String nickname;

    private boolean social;

    @ElementCollection(fetch = LAZY)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    public void addRole(MemberRole memberRole) {
        memberRoleList.add(memberRole);
    }

    public void clearRole() {
        memberRoleList.clear();
    }

}
