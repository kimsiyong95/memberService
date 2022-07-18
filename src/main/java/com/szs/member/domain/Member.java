package com.szs.member.domain;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        ObjectMapper mapper = new ObjectMapper();
        if(scrapData!=null){
            try {
                this.scrapData = mapper.writeValueAsString(scrapData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
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
        Map map = new LinkedHashMap();

        map.put("이름", name);


        if(this.scrapData==null){
            map.put("message", "스크랩된 데이터가 없습니다.");
            return map;
        }

        JsonElement element = JsonParser.parseString(this.scrapData);

        JsonObject infos = element.getAsJsonObject()
                .get("data").getAsJsonObject()
                .get("jsonList").getAsJsonObject();



        double limitAmount = limitCalculation(infos);
        double calculatedTax = deductibleCalculation(infos);

        map.put("한도",  numberToUnit(limitAmount));
        map.put("공제액", numberToUnit(calculatedTax));
        map.put("환급액", numberToUnit(Math.min(limitAmount, calculatedTax)));

        return map;

    }

    public double deductibleCalculation(JsonObject jsonObject){
        int calculatedTax = 0;

        for(JsonElement scrap002 : jsonObject.get("scrap002").getAsJsonArray()){
            calculatedTax += Integer.valueOf(
                    scrap002.getAsJsonObject()
                            .get("총사용금액")
                            .getAsString()
                            .replaceAll(",","")
                            .replaceAll("\\.",""));
        }

        if(calculatedTax <= 1300000){
            return calculatedTax * 0.55;
        }else if(calculatedTax > 1300000){
            return (calculatedTax - 1300000)*0.3 + 715000;
        }

        return 0;
    }

    public double limitCalculation(JsonObject jsonObject){

        if(jsonObject==null||jsonObject.get("scrap001").isJsonNull()){
            return 740000;
        }

        int totalPrice = 0;

        for(JsonElement scrap001 : jsonObject.get("scrap001").getAsJsonArray()){
            totalPrice += Integer.valueOf(
                    scrap001.getAsJsonObject()
                            .get("총지급액")
                            .getAsString()
                            .replaceAll(",","")
                            .replaceAll("\\.",""));
        }

        if(totalPrice > 33000000 && totalPrice <= 70000000){
            return (740000 - ((totalPrice - 33000000) * 0.008))<660000 ? 660000: (int) (740000 - ((totalPrice - 33000000) * 0.008));
        }else if(totalPrice > 70000000){
            return (660000 - ((totalPrice - 70000000) * 0.5)) < 500000 ? 500000 : (int) (660000 - ((totalPrice - 70000000) * 0.5));
        }

        return 740000;

    }

    public String numberToUnit(double money){

        StringBuilder stringBuilder = new StringBuilder();

        int m1=(int)(money/10000);
        int m2= (int) ((money%10000)/1000);
        int m3= (int) ((money%1000)/100);

        if(m1 != 0){
            stringBuilder.append(m1+"만");
        }

        if(m2 != 0){
            stringBuilder.append(m2+"천");
        }

        if(m3 != 0){
            stringBuilder.append(m3+"백");
        }

        stringBuilder.append("원");

        return stringBuilder.toString();
    }

}
