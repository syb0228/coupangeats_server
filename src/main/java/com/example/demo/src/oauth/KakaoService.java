package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.src.oauth.model.OAuthLoginRes;
import com.example.demo.src.oauth.model.SocialUserInfo;
import com.example.demo.src.oauth.model.UserInfo;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.*;


@Service
@RequiredArgsConstructor
@Transactional
public class KakaoService {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    private final KakaoProvider kakaoProvider;

    private final JwtService jwtService;

    private final UserInfoDao userInfoDao;

    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private final String GRANT_TYPE = "authorization_code";
    private final String REDIRECT_URI = "http://localhost:9000/auth/kakao/callback";

    public OAuthLoginRes kakaoLogin(String code) throws JsonProcessingException, BaseException {

        // 1. 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken(code);

        // 2. 액세스 토큰으로 카카오 API 호출
        SocialUserInfo kakaoSocialUserInfo = getKakaoUserInfo(accessToken);

        // 3. 존재하지 않는 회원이면 회원 가입 진행
        if(kakaoProvider.checkEmail(kakaoSocialUserInfo.getEmail()) == 0){
            createUser(kakaoSocialUserInfo);
        }

        // 4. 로그인
        OAuthLoginRes oAuthLoginRes = kakaoProvider.logIn(kakaoSocialUserInfo.getEmail());
        return oAuthLoginRes;
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // http header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", CONTENT_TYPE);

        // http body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        // http 요청
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // http 응답 (json) -> 액세스 토큰 parsing
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private SocialUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // http header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", CONTENT_TYPE);

        // http 요청
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // response body 정보 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickName = jsonNode.get("properties").get("nickname").asText();

        return new SocialUserInfo(id, nickName, email);
    }

    public UserInfo createUser(SocialUserInfo kakaoSocialUserInfo) throws BaseException {
        String pwd;
        try {
            // 암호화
            pwd = new SHA256().encrypt(UUID.randomUUID().toString());
            UserInfo kakaoUser = new UserInfo(kakaoSocialUserInfo.getEmail(), pwd, kakaoSocialUserInfo.getNickname());

            int userId = userInfoDao.createUser(kakaoUser);
            // jwt 발급
            jwtService.createJwt(userId);

            return kakaoUser;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
