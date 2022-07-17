package com.szs.member.domain;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.szs.member.common.encryption.AES256;
import com.szs.member.common.request.MemberSignUpRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userNo;

    private String userId;
    private String password;
    private String name;
    private String regNo;
    private String roles;
    @Column(columnDefinition="TEXT", length = 2048)
    private String scrapData;

    public static Member createMember(MemberSignUpRequestDTO memberRequestDTO
            , PasswordEncoder passwordEncoder) {

        try {
            AES256 aes256 = new AES256();

            return Member.builder()
                    .userId(memberRequestDTO.getUserId())
                    .password(passwordEncoder.encode(memberRequestDTO.getPassword()))
                    .name(memberRequestDTO.getName())
                    .regNo(aes256.encrypt(memberRequestDTO.getRegNo()))
                    .roles("ROLE_USER")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateScrapData(Map<String, Object> scrapData){
        if(scrapData!=null){
            this.scrapData = scrapData.toString();
        }
    }


    public List<String> getRoleList() {
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }

        return new ArrayList<>();
    }

    public Map getScrapSendData(){
        Map map = new HashMap();

        try {
            AES256 aes256 = new AES256();
            map.put("name", name);
            map.put("regNo",aes256.decrypt(regNo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public Map getMyRefund(){
        Map map = new HashMap();

        map.put("이름", name);


        if(this.scrapData==null){
            map.put("message", "스크랩된 데이터가 없습니다.");
            return map;
        }
        JsonElement element = JsonParser.parseString(this.scrapData);

        JsonArray infos = element.getAsJsonObject()
                .get("data").getAsJsonObject()
                .get("jsonList").getAsJsonArray();





//            "한도": "68만 4천원",
//            "공제액": "92만 5천원",
//            "환급액": "68만 4천원"


        return map;

    }


    public String limitCalculation(){
        return "";
    }

}
