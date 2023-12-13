package org.iclass.mvc.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.mvc.dao.UserMapper;
import org.iclass.mvc.dto.HeartHomeDto;
import org.iclass.mvc.dto.MailDto;
import org.iclass.mvc.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;


@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j

public class UserService {
    private final UserMapper dao;

    //회원 가입
    public int insert(UserDto dto) {
        return dao.insert(dto);
    }

    //조인한 하트와 홈 메소드
    public List<HeartHomeDto> selectHeartbyEmail (String email){
        List<HeartHomeDto> list = dao.selectHeartbyEmail(email);
        return list;
    }

    //카카오로 로그인 할 때 디비에 저장되는 기준. 카카오Dto를 따로 만들지 않고 UserDto와 같이 사용.
    public void kakoInsert(UserDto userDto) {
        dao.kakaoInsert(userDto);
    }
    //카카오로그인(이메일기준, 아이디는 시퀀스 써서 idx로 사용 예정)
    public UserDto kakaoLogin(String email) {
        return dao.kakaoLogin(email);
    }


    //일반로그인(id가 시퀀스로 idx처럼 쓰이기 때문에 email과 password로 로그인하게 함)
    public UserDto login(UserDto userDto) {
        log.info("service: {}", userDto.getPassword());
        log.info("UserDto.getEmail: {}", userDto.getEmail());
        UserDto result = dao.login(userDto);
        return result;
    }

    public int updateInfo(UserDto userDto) {
        return dao.updateInfo(userDto);
    }

    public UserDto userInfoById(String id) {
        return  dao.userInfoById(id);
    }

    //사업자 분류
    public Boolean business(String email) {return dao.business(email); }

    //내정보찾기
    public UserDto userInfo(String email) {
        return dao.userInfoByEmail(email);
    }



    //아이디 찾기
    public UserDto findId(UserDto userDto){return dao.findId(userDto);}
    
    //////////////////////////비밀번호///////////////////////////////////////////

    @Autowired
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "qkrenghks05@naver.com";

    //패스워드 난수 생성기
    private String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    public void mailSend(MailDto mailDto){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        mailSender.send(message);
    }

    @Transient
    public void sendFindPassword(String userEmail, String userName) throws Exception {
        // 이메일과 이름이 같은가?
        boolean check = dao.checkUserEmailAndName(userEmail, userName);
        if (!check) throw new Exception("이메일과 이름이 일치하지 않음");
        // 같으면 임시 비밀번호 생성
        String newPassword = getTempPassword();
        // 임시 비밀번호 저장 DB
        dao.updateidpw(userEmail, newPassword);
        MailDto mailDto = MailDto.createMailAndChangePassword(userEmail, userName, newPassword);

        // 메일발송
        mailSend(mailDto);
    }
}

