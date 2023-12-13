package org.iclass.mvc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class MailDto {
    private String address;
    private String title;
    private String message;

    public static MailDto createMailAndChangePassword(String userEmail, String userName, String newPassword) {
        MailDto dto = new MailDto();
        dto.setAddress(userEmail);
        dto.setTitle(userName+"님의 숙박사 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 숙박사 임시비밀번호 안내 관련 이메일 입니다." + "[" + userName + "]" +"님의 임시 비밀번호는 "
                + newPassword + " 입니다.");
        return dto;
    }
}