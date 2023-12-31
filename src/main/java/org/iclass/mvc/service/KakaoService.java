package org.iclass.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.mvc.dto.UserDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoService {

    private final UserService userService;

    @Value("${kakao.client.id}") // value의 옵션을 설정해서 application properties의 key의 값을 가져온다.
    private String kakaoClientId;
    @Value("${kakao.client.secret}")
    private String kakaoClientSecret;
    @Value("${kakao.redirect.url}")
    private String kakaoRedirectUrl;


    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com"; // 이 요청으로 보내야 카카오측에서 인증을 해줌
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize" //rest api 가져올 때 경로설정
                + "?client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUrl
                + "&response_type=code";

    }
    public UserDto getKakaoInfo(String code) throws Exception{

        if (code == null) throw new Exception("Failed get authorization code");

        String accessToken = "";
        String refreshToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type"   , "authorization_code");
            params.add("client_id"    , kakaoClientId);
            params.add("client_secret", kakaoClientSecret);
            params.add("code"         , code);
            params.add("redirect_uri" , kakaoRedirectUrl);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            accessToken  = (String) jsonObj.get("access_token");
            refreshToken = (String) jsonObj.get("refresh_token");
        } catch (Exception e) {
            e.printStackTrace();

            throw new Exception("API call failed");
        }

        return getUserInfoWithToken(accessToken);
    }

    private UserDto getUserInfoWithToken(String accessToken) throws Exception {
        //HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );



        //Response 데이터 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());


        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

//        String id = (String) jsonObj.get("id");

        String email = String.valueOf(account.get("email"));
        String gender = String.valueOf(account.get("gender"));
        String name = String.valueOf(profile.get("nickname"));
        gender = gender.equals("female")?"F":"M";

        log.info("================");
        log.info(email);
        log.info(gender);
        log.info(name);

        log.info("================");

        UserDto user;

        user = userService.kakaoLogin(email);

        if(user == null){

            UserDto newUser = new UserDto();
            newUser.setEmail(email);
            newUser.setGender(gender);
            newUser.setName(name);
            userService.kakoInsert(newUser);


            user = userService.kakaoLogin(email);
        }




        return user;
    }
    public String getKakaoLogout(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/logout",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );
            log.info("-------------------------------");
            log.info(accessToken);
            log.info("--------------------------------------");

            // 로그아웃 성공 여부에 따라 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                return "로그아웃 성공";
            } else {
                return "로그아웃 실패";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "로그아웃 실패";
        }
    }


}

