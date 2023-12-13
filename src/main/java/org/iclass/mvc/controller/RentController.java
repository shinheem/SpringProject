package org.iclass.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.mvc.dto.HomeDto;
import org.iclass.mvc.dto.RentDto;
import org.iclass.mvc.dto.UserDto;
import org.iclass.mvc.service.HomeService;
import org.iclass.mvc.service.RentService;
import org.iclass.mvc.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rent")
public class RentController {
    private final RentService rentService;
    private final HomeService homeService;
    private final UserService userService;

    //-김민섭-예약내역
    @GetMapping("/reserpage")
    public void userent(Model model,
                        HttpSession session, @RequestParam(defaultValue = "1") int page) {
        String email = (String) session.getAttribute("email");
        Map<String, Object> pagelistResult = rentService.pagelist(page, email);
        log.info("pagelist: {}",pagelistResult);
        model.addAttribute("list", pagelistResult.get("list"));
        model.addAttribute("paging", pagelistResult.get("paging"));
    }
    @GetMapping("/reserForm")
    public String reserForm(Long homeIdx, Model model) {
        RentDto rentDto = new RentDto();
        model.addAttribute("homeInfo", homeService.detail(homeIdx));
        model.addAttribute("rentDto", rentDto);
        return "rent/reserForm";
    }

    @PostMapping("/reserForm")
    public String insertRent(RentDto rentDto, Model model, Long homeIdx) {
        rentService.insert(rentDto);
        int rentno = rentDto.getRentno();
        log.info("__________________________________");
        log.info("__________________________________");
        log.info("rentDto{}"+rentDto.toString());
        model.addAttribute("homeInfo", homeService.detail(homeIdx));


        model.addAttribute("rentno", rentno);

        return "redirect:/rent/pay?homeIdx="+homeIdx+"&rentno="+rentno;
    }

    @GetMapping("/pay")
    public String pay(Model model, HttpSession session,
                      Long homeIdx, int rentno) {
        String email = (String) session.getAttribute("email");
        UserDto userDto = userService.userInfo(email);
        HomeDto homeInfo = homeService.selectOne(homeIdx);

        model.addAttribute("userDto", userDto);
        model.addAttribute("email", email);
        model.addAttribute("homeInfo", homeInfo);
        model.addAttribute("rentno", rentno);

        RentDto rentDto = rentService.selectByrentno(rentno);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 문자열을 LocalDate로 변환
        LocalDate date1 = LocalDate.parse(String.valueOf(rentDto.getCheckout()), formatter);
        LocalDate date2 = LocalDate.parse(String.valueOf(rentDto.getCheckin()), formatter);


        int daysBetween = (int) ChronoUnit.DAYS.between(date2,date1);

        model.addAttribute("daysBetween", daysBetween);
        model.addAttribute("rentDto", rentDto);

        return "rent/pay";
    }

    @PostMapping("/pay")
    public String sendpay(HttpSession session, Model model, Long homeIdx, @RequestParam("rentno") int rentno) {

        String email = (String) session.getAttribute("email");
        UserDto userDto = userService.userInfo(email);
        RentDto rentDto = rentService.selectByrentno(rentno);
        session.setAttribute("homeIdx", homeIdx);
        model.addAttribute("homeInfo", homeService.detail(homeIdx));
        model.addAttribute("rentDto", rentDto);
        model.addAttribute("userDto", userDto);

        rentService.insert(rentDto);
        log.info(String.valueOf(rentService.insert(rentDto)));

        return "redirect:/pay/viewpage?homeIdx="+homeIdx+"&rentno="+rentno;
    }


    @PostMapping("/delete")
    public String delete(@ModelAttribute RentDto rentDto, RedirectAttributes re){
        int rentno = rentDto.getRentno();
        rentService.delete(rentno);
        re.addFlashAttribute("result", "예약 취소 완료되었습니다. (" + rentno + "번)");
        return "redirect:/rent/reserpage";
    }





}