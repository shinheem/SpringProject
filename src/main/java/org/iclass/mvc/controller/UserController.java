package org.iclass.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.iclass.mvc.dto.HeartHomeDto;
import org.iclass.mvc.dto.MailDto;
import org.iclass.mvc.dto.UserDto;
import org.iclass.mvc.service.KakaoService;
import org.iclass.mvc.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService service;
    private final KakaoService kakaoService;


    // 찜목록 출력
    @GetMapping("/heartsList")
    public void heartsList(HttpSession session,Model model){
        String email = (String) session.getAttribute("email");
        List<HeartHomeDto> list = service.selectHeartbyEmail(email);
        model.addAttribute("heart", list);
    }
    @GetMapping("/info")
    public void userInfo(Model model, RedirectAttributes redirectAttributes,HttpSession session) {
        String id = (String) session.getAttribute("id");    //세션에서 유저 아이디 가져오기
        String email = (String) session.getAttribute("email");
        UserDto userDto = null;
        if(id!=null){
            userDto = service.userInfoById(id); //유저정보 DTO에서 가져오기
        }else {
            userDto = service.userInfo(email); //유저정보 DTO에서 가져오기
        }
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        log.info("userDto >> {}" , userDto);
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        model.addAttribute("userDto", userDto); //모델에 추가하기
    }


    @GetMapping("/infoupdate")
    public void infoUpdateForm(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        UserDto dto = service.userInfo(email);
        model.addAttribute("userDto", dto);
    }

    // 내정보 수정 처리
    @PostMapping("/infoupdate")
    public RedirectView infoUpdate(UserDto userDto, RedirectAttributes redirectAttributes) {
        service.updateInfo(userDto);
        redirectAttributes.addFlashAttribute("successMessage", "내 정보가 성공적으로 수정되었습니다.");
        return new RedirectView("/user/info") ;
    }

    @GetMapping("/login")
    public void login(Model model) {
        model.addAttribute("kakaoUrl",kakaoService.getKakaoLogin());
    }

    @PostMapping("/login")
    public String login(Model model,RedirectAttributes redirectAttributes, HttpSession session, UserDto userDto) {
        log.info("++++++++++++++++++++++++++++++++++");
        log.info("1>>>{}",userDto.getEmail());
        log.info("2>>>{}",userDto);
        UserDto user = service.login(userDto);
        log.info("3>>>{}",user);
        log.info("+++++++++++++++++++++++++++++++++++++++++");

        String url = "/";
        if (user == null) {
            redirectAttributes.addFlashAttribute("incorrect", "y");
            url = "login";
        } else {
            // model.addAttribute("id", user.getId());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("password", user.getPassword());
            redirectAttributes.addFlashAttribute("message", "로그인완료");
        }

        return "redirect:" + url;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";


    }

    //회원 가입 창
    @GetMapping("/signup")
    public void signup() {

    }

    //아이디 찾기
    @PostMapping("/findId")
    @ResponseBody
    public Map<String, Object> findIdAction(UserDto userDto) {
        Map<String,Object> json = new HashMap<>();

        UserDto user = service.findId(userDto);
        if(user == null) {
            json.put("check", "false");
            json.put("msg", "회원님의 이름과 전화번호로 찾을 수 있는 이메일이 없습니다.");
        } else {
            json.put("check", "true");
            json.put("msg", userDto.getName() + "님의 이메일은 " + user.getEmail() + "입니다.");
        }

        return json;
    }

    //Email과 name의 일치여부를 check하는 컨트롤러
    @PostMapping("/findPassword")
    @ResponseBody
    public Map<String, Object> pwFind(String userEmail, String userName) throws Exception{
        Map<String,Object> json = new HashMap<>();

        try {
            service.sendFindPassword(userEmail, userName);
            json.put("check", "true");
            json.put("msg", "새 비밀번호를 "+userEmail+"로 전송했습니다.");
        } catch (Exception e) {
            json.put("check", "false");
            json.put("msg", e.getMessage());
        }

        //pwFindCheck 이 참이면 이멩일 보내기 -> 이베일 보넀다. 메시지비도 전달.
        return json;
    }





}



