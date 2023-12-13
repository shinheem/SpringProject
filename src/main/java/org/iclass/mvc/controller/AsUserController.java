package org.iclass.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.mvc.dao.UserMapper;
import org.iclass.mvc.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class AsUserController {

    private final UserMapper UserMapper;


    @GetMapping("/user/check/{email}")
    public Map<String,Boolean> check (@PathVariable String email){
        int count = UserMapper.checkDuplicateEmail(email);
        Map<String,Boolean>resultMap = new HashMap<>();
        resultMap.put("exist", (count == 1));
        return resultMap;
    }

    //@Valid는 유효성 검사
    @PostMapping("/users")
    public Map<String,Integer> save(@RequestBody @Valid UserDto userdto){
        int count = UserMapper.insert(userdto);
        Map<String,Integer> resultMap = new HashMap<>();
        resultMap.put("count",count);

        return resultMap;
    }
}
