package org.iclass.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.iclass.mvc.dto.UserDto;
import org.iclass.mvc.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
//    public ResponseEntity<String> callback(HttpServletRequest request) throws Exception {
    public RedirectView callback(@RequestParam("code")String code, HttpServletRequest req) throws Exception {
        UserDto kakaoInfo = kakaoService.getKakaoInfo(code);

        req.getSession().setAttribute("id", kakaoInfo.getId());
        req.getSession().setAttribute("email", kakaoInfo.getEmail());
        req.getSession().setAttribute("name", kakaoInfo.getName());

        return new RedirectView("/");

    }
    @GetMapping("/logout")
    public RedirectView kakaoLogout(HttpServletRequest request, HttpSession httpSession) {
        // 세션 무효화
        if (httpSession != null) {
            httpSession.invalidate();
        }

        String kakaoAccessToken = request.getParameter("kakaoAccessToken");


        String result = kakaoService.getKakaoLogout(kakaoAccessToken);

        return new RedirectView("/");
    }

}

