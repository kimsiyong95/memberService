package com.szs.member.repository;

import com.szs.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member findByUserIdOrRegNo(String userId, String regNo);

    public Member findByUserId(String userId);
}
