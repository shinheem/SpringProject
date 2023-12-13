package org.iclass.mvc.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.mvc.dto.HomeDto;
import org.iclass.mvc.dto.PaySuccessRestDto;
import org.iclass.mvc.dto.RentDto;
import org.iclass.mvc.dto.UserDto;
import org.iclass.mvc.service.HomeService;
import org.iclass.mvc.service.RentService;
import org.iclass.mvc.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
@RequestMapping("/pay")
@Slf4j
public class PaymentController {
    private final RentService rentService;
    private final HomeService homeService;
    private final UserService userService;

    @GetMapping("/viewpage")
    public String showviewpage(Model model, HttpSession session,  Long homeIdx,int rentno) {
        // homeIdx를 Long 타입으로 변경

        String email = (String) session.getAttribute("email");
        UserDto userDto = userService.userInfo(email);
        HomeDto homeInfo = homeService.selectOne(homeIdx);

        model.addAttribute("userDto", userDto);
        model.addAttribute("email", email);
        model.addAttribute("homeInfo", homeInfo);
        model.addAttribute("rentno", rentno);



        RentDto rentDto = rentService.selectByrentno(rentno);

        log.info("==================");
        log.info(rentDto.toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 문자열을 LocalDate로 변환
        LocalDate date1 = LocalDate.parse(String.valueOf(rentDto.getCheckout()), formatter);
        LocalDate date2 = LocalDate.parse(String.valueOf(rentDto.getCheckin()), formatter);


        int daysBetween = (int)ChronoUnit.DAYS.between(date2,date1);

        model.addAttribute("daysBetween", daysBetween);
        model.addAttribute("rentDto", rentDto);


        return "pay/viewpage";
    }




    @GetMapping("/fail")
    public void fail() {

    }

    @GetMapping("/success")
    public String payMentSuccess(@RequestParam String paymentKey, @RequestParam String orderId,
                                 @RequestParam int amount) throws IOException, InterruptedException {
        // orderId와 amount를 JSON 문자열로 변환
        //String requestBody = String.format("{\"orderId\":\"%s\",\"amount\":%d}", orderId, amount);

        log.info("paymentKey : {}", paymentKey);
        log.info("orderId : {}", orderId);
        log.info("amount : {}", amount);


        String testSecretApiKey = "test_sk_LkKEypNArWDYdjwXYe7A8lmeaxYG" + ":";
        String authKey = new String(Base64.getEncoder().encode(testSecretApiKey.getBytes(StandardCharsets.UTF_8)));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Authorization", "Basic " + authKey)
                .header("Content-Type", "application/json")
                .method("POST"
                        , HttpRequest
                                .BodyPublishers
                                .ofString("{\"paymentKey\":\"" + paymentKey + "\",\"amount\":\"" + amount + "\",\"orderId\":\"" + orderId + "\"}")
                ).build();

        HttpResponse<String> response = HttpClient
                .newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        log.info("response body : {}",response.body());


        ObjectMapper objectMapper = new ObjectMapper();         //자바객체와 json 사이의 직렬화, 역직렬화 메소드를 제공. (spring web 에 포함)
        //json으로 받은 string 데이터를 객체화
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PaySuccessRestDto successResDto = objectMapper.readValue(response.body(), PaySuccessRestDto.class);
        //PaySuccessResDto successResDto = objectMapper.readValue(response.body(),PaySuccessResDto.class);
        log.info("successResDto : {}",successResDto);
        //테이블(결제 내역 테이블: 사용자이메일, 상품ID , 가격, rentno(렌트테이블 PK) , 결제날짜, 결제방법 ,
        //데이터 insert 할 테이블 만들고 dto도 새로 만들어준다

        //
//        return "redirect:"+ successResDto.getCheckout().get("url");

        log.info("================================");
        log.info(successResDto.getCheckout().get("url"));
        log.info("================================");

        return "redirect:/rent/reserpage";
    }

}