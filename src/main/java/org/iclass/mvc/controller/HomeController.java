package org.iclass.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.iclass.mvc.dto.*;
import org.iclass.mvc.service.HomeService;
import org.iclass.mvc.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
@Log4j2
public class HomeController {

    private final HomeService service;

    @GetMapping("/list")
    public void pagelist(PageRequestDTO pageRequestDTO, Model model) {

        PageResponseDTO responseDTO = service.listwithSearch(pageRequestDTO);
        responseDTO.getList().forEach(i-> {
            log.info(">>>>> 글 : {}" ,i);
        });
        model.addAttribute("paging", responseDTO);
        model.addAttribute("today", LocalDate.now());
    }

    //숙소 등록
    @PostMapping("/save")
    public String insert(HomeDto dto) {
        int count = service.insert(dto);
        return "redirect:/home/list";
    }
    //숙소 등록창
    @GetMapping("/hregister")
    public void register() {

    }

    @GetMapping("/detail")
    public void detail(long idx, HomeDto dto, Model model, HttpSession session,@RequestParam(defaultValue = "1") int page) {
        String email = (String)session.getAttribute("email");
        if(email != null){
        HomeDto list = service.detail(idx);
        model.addAttribute("list", list);
        List<Integer> myhearts = service.myhearts(email);
        model.addAttribute("myhearts",myhearts);
        model.addAttribute("rlist", service.reviewPage(page,idx).get("list"));
        model.addAttribute("rpaging", service.reviewPage(page,idx).get("paging"));
        }else {
            HomeDto list = service.detail(idx);
            model.addAttribute("list", list);
            model.addAttribute("myhearts",null);
            model.addAttribute("rlist", service.reviewPage(page,idx).get("list"));
            model.addAttribute("rpaging", service.reviewPage(page,idx).get("paging"));
        }

    }

    /* 리뷰 */
    @PostMapping("/detail")
    public String detailWrite(ReviewDto reviewDto){
        service.reviewInsert(reviewDto);
        log.info("dto = {}", reviewDto);
        return "redirect:/home/detail?idx=" + reviewDto.getIdx();
    }

}
