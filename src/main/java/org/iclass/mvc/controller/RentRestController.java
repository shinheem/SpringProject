package org.iclass.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.iclass.mvc.service.RentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentRest")
public class RentRestController {
    private final RentService rentService;
    @GetMapping("/disabledate/{homeIdx}")
    public List<String> sendDisabledate(@PathVariable("homeIdx") Long homeIdx){
        System.out.println("====================================");
        System.out.println(rentService.selectDisableDates(homeIdx));
        System.out.println("====================================");
        return rentService.selectDisableDates(homeIdx);
    }

}
