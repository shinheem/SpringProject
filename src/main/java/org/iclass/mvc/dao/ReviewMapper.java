package org.iclass.mvc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.iclass.mvc.dto.ReviewDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewMapper {
    List<ReviewDto> selectAll(long idx);
    List<ReviewDto> reviewlist(Map<String,Long> map);
    int count(long idx);
    int reviewInsert(ReviewDto reviewDto);
    int reviewUpdate(ReviewDto reviewDto);
    int reviewDelete(long rnum);
}
