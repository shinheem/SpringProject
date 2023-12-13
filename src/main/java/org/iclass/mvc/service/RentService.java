package org.iclass.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.iclass.mvc.dao.RentDao;
import org.iclass.mvc.dto.HomeDto;
import org.iclass.mvc.dto.Paging;
import org.iclass.mvc.dto.RentDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class RentService {

    private final RentDao dao;

    public int insert(RentDto rentDto) {
        return dao.insert(rentDto);
    }
    public List<RentDto> userent(String email) {
        return dao.userent(email);
    }

    public RentDto selectByrentno(int rentno) {
        return dao.selectByrentno(rentno);
    }

    public Map<String, Object> pagelist(int page, String email) {
        int pageSize = 5;
        int totalCount = dao.count(email);
        Paging paging = new Paging(page, totalCount, pageSize);

        Map<String, Object> map = new HashMap<>();
        map.put("start", paging.getStartNo());
        map.put("end", paging.getEndNo());
        map.put("email", email);

        List<RentDto> list = dao.pagelist(map);

        Map<String, Object> result = new HashMap<>();
        result.put("paging", paging);
        result.put("list", list);

        return result;
    }

    public List<String> selectDisableDates(Long homeIdx){
        return dao.selectDisableDates(homeIdx);
    }

    //삭제 기능
    public int delete(int rentno) {   //Integer, Long : 래퍼(wrapper) 클래스
        return dao.delete(rentno);
    }


}

