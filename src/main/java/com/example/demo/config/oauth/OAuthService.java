package com.example.demo.config.oauth;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class OAuthService {

//    public String getKakaoAccessToken(String code){
//        String accessToken = "";
//        String refreshToken = "";
//        String requestUrl = "https://kauth.kakao.com/oauth/token";
//
//        try {
//            URL url = new URL(requestUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))
//
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//        return accessToken;
//
//    }

}
