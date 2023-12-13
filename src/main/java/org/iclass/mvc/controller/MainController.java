package org.iclass.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class MainController {


    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest req){
        model.addAttribute("email", req.getSession().getAttribute("email") );

        return "index";
    }
}
