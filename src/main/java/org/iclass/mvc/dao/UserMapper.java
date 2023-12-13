package org.iclass.mvc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.iclass.mvc.dto.HeartHomeDto;
import org.iclass.mvc.dto.MailDto;
import org.iclass.mvc.dto.UserDto;

import java.util.List;

@Mapper
public interface UserMapper {

    int updateInfo(UserDto userDto); //내 정보 수정


    List<HeartHomeDto> selectHeartbyEmail(String email);    //찜목록
    int insert(UserDto dto); //회원가입
    int checkDuplicateEmail(String email);  //중복확인

    void kakaoInsert(UserDto userDto); // 카카오회원 등록

    UserDto kakaoLogin(String email);  //카카오 로그인

    UserDto login(UserDto userDto);  //일반로그인


    UserDto userInfoById(String id);

    UserDto findId(UserDto userDto); // 아이디 찾기

    UserDto userInfoByEmail(String email);

    //비밀번호 업데이트
    int updateidpw(String email,String password);

    MailDto createMailAndChangePassword(String userEmail, String userName);

    boolean checkUserEmailAndName(String userEmail, String userName);

    Boolean business(String email);

}