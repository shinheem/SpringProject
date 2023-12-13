package org.iclass.mvc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.iclass.mvc.dto.HeartDto;
import org.iclass.mvc.dto.HomeDto;
import org.iclass.mvc.dto.PageRequestDTO;

import java.util.List;

@Mapper
public interface HomeMapper {
    //숙소 리스트
    List<HomeDto> pagelist(PageRequestDTO pageRequestDTO);
    /*List<HomeDto> getList();*/
    //글 수정, 글 읽기
    int count(PageRequestDTO pageRequestDTO);
    //숙소 등록
    int insert(HomeDto dto);
    //조회수
    int setReadCount(long idx);

    //상품상세출력
    HomeDto selectOne(long idx);
    //idx 조회
    HomeDto selectByIdx(long idx);
///////////////////////////////////////
    void heartTrue(HeartDto dto);   //좋아요 등록
    void heartFalse(HeartDto dto);  //좋아요 취소

    int updateHeartCount(long idx);  //좋아요 갯수 업데이트
    long selectByHeartIdx(long idx);  //좋아요 갯수 조회

 /*   List<HomeDto> checkheart(HomeDto dto);*/
    List<Integer> myhearts(String email);
}
