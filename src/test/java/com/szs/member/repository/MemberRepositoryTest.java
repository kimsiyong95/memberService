package com.szs.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.szs.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void setUp(){
        Member member = Member.builder()
                .userId("kimsiyong")
                .name("김시용")
                .regNo("123456-123456")
                .build();

        Member saveMember = memberRepository.save(member);
    }


    @DisplayName("repo 주입 테스트")
    @Test
    public void repoNotNullTest(){
        assertThat(memberRepository).isNotNull();
    }


    @DisplayName("사용자 정보 조회 테스트")
    @Test
    public void findByUserIdTest(){
        Member findMember = memberRepository.findByUserId("kimsiyong");

        assertThat("kimsiyong").isEqualTo(findMember.getUserId());

    }

    @DisplayName("아이디 또는 주민번호 중복 조회 테스트")
    @Test
    public void findByUserIdOrRegNoTest(){
        Member findMember = memberRepository.findByUserIdOrRegNo("kimsiyong", "1111111-11111");

        assertThat("kimsiyong").isEqualTo(findMember.getUserId());
    }
}