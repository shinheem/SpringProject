package org.iclass.mvc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.iclass.mvc.dto.HomeDto;
import org.iclass.mvc.dto.RentDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface RentDao {
    //예약 등록
    int insert(RentDto rentDto);
    //에약 확인
    RentDto selectByrentno(int rentno);

    List<RentDto> userent(String email);

    //글목록 페이징
    List<RentDto> pagelist(Map<String, Object> map);
    int count(String email);

    int delete(int rentno);

    List<String> selectDisableDates(Long homeIdx);




}