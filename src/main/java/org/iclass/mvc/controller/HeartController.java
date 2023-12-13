package org.iclass.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.iclass.mvc.dto.HeartDto;
import org.iclass.mvc.service.HomeService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class HeartController {
    private final HomeService service;

    @PostMapping("/heart")
    public Map<String,Object> heartTrue(@RequestBody HeartDto dto, HttpSession session) {
        String email = (String)session.getAttribute("email");
        long count = service.heartProcess(dto);
        log.info("id = {}",email);
        log.info("heart dto : {}",dto);
        //  int count=service.heartProcess(dto);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("heartcount",count);
        return resultMap;
    }







}
