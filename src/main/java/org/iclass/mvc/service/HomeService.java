package org.iclass.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.mvc.dao.HomeMapper;
import org.iclass.mvc.dao.ReviewMapper;
import org.iclass.mvc.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class HomeService {

    private final HomeMapper dao;
    private final ReviewMapper mapper;

    public PageResponseDTO listwithSearch(PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(6);
        pageRequestDTO.setDatas();
        log.info("pageRequestDTO:{}",pageRequestDTO );
        List<HomeDto> list = dao.pagelist(pageRequestDTO);
        PageResponseDTO responseDTO = PageResponseDTO.of(pageRequestDTO,dao.count(pageRequestDTO),6);
        responseDTO.setList(list);
        return responseDTO;
    }
    

    //상품 상세목록 출력
    public HomeDto selectOne(long idx){
        HomeDto list = dao.selectOne(idx);
        return list;
    }
    //숙소저장
    public int insert(HomeDto dto) {
        String path = "D:\\iclassMINSOEB\\upload";
        StringBuilder filenames = new StringBuilder();  //테이블 컬럼으로 전달될 파일명들(,로 구분)
        //파일 업로드
        for(MultipartFile f:dto.getPics()) {
            if(f.getSize()!=0) {
                String ofilename = f.getOriginalFilename();
                String postfix = ofilename.substring(ofilename.lastIndexOf("."));
                StringBuilder newfile = new StringBuilder("gallary_")
                        .append(UUID.randomUUID().toString().substring(0,8)).append(postfix);
                File file = new File(path + "\\"+newfile);
                try {
                    f.transferTo(file);
                    filenames.append(newfile).append(",");
                }catch (IOException e) { }
            }
        }
        dto.setFilenames(filenames.toString());
        return dao.insert(dto);
    }

    public HomeDto detail(long idx) {
        dao.setReadCount(idx);
        return dao.selectByIdx(idx);
    }

    ////////////////좋아요////////////////////////
    public long heartProcess(HeartDto dto) {
        long count = 0;
        long idx = dto.getIdx();
        if(dto.isStatus()) {
            dao.heartTrue(dto);
            dao.updateHeartCount(idx);
            count = dao.selectByHeartIdx(idx);
        }else {
            dao.heartFalse(dto);
            dao.updateHeartCount(idx);
            count = dao.selectByHeartIdx(idx);
        }
        return count;
    }

    public List<Integer> myhearts(String email) {
        return dao.myhearts(email);
    }
    /* 리뷰 */

    public List<ReviewDto> selectAll(long idx){
        List<ReviewDto> list = mapper.selectAll(idx);
        return list;
    }
    public Map<String, Object> reviewPage(int page,long idx) {

        int pageSize = 6;
        int totalCount = mapper.count(idx);

        Paging paging = new Paging(page, totalCount, pageSize);

        Map<String, Long> map = new HashMap<>();
        map.put("start", paging.getStartNo());
        map.put("end", paging.getEndNo());
        map.put("idx", idx);

        List<ReviewDto> list = mapper.reviewlist(map);

        Map<String,Object> result = new HashMap<>();
        result.put("paging", paging);
        result.put("list", list);

        return result;
    }

    public int reviewInsert(ReviewDto reviewDto) {
        String path = "D:\\iclassMINSOEB\\upload";
        StringBuilder filenames = new StringBuilder();  //테이블 컬럼으로 전달될 파일명들(,로 구분)
        //파일 업로드
        for(MultipartFile f:reviewDto.getPics()) {
            if(f.getSize()!=0) {
                String ofilename = f.getOriginalFilename();
                String postfix = ofilename.substring(ofilename.lastIndexOf("."));
                StringBuilder newfile = new StringBuilder("gallary_")
                        .append(UUID.randomUUID().toString().substring(0,8)).append(postfix);
                File file = new File(path + "\\"+newfile);
                try {
                    f.transferTo(file);
                    filenames.append(newfile).append(",");
                }catch (IOException e) { }
            }
        }
        reviewDto.setFilenames(filenames.toString());
        return mapper.reviewInsert(reviewDto);
    }
    public int reviewUpdate(ReviewDto reviewDto) {
        return mapper.reviewUpdate(reviewDto);
    }
    public int reviewDelete(long rnum){
        return mapper.reviewDelete(rnum);
    }
}
