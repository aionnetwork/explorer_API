package com.aion.dashboard.services;

import com.aion.dashboard.utility.RecaptchaUtil;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
public class RecaptchaService {
    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    private static final String GOOGLE_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    public String verifyRecaptcha(String ip, String recaptchaResponse) {
        Map<String, String> body = new HashMap<>();
        body.put("secret", recaptchaSecret);
        body.put("response", recaptchaResponse);
        body.put("remoteip", ip);
        ResponseEntity<Map> recaptchaResponseEntity = restTemplateBuilder.build()
                .postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL + "?secret={secret}&response={response}&remoteip={remoteip}", body, Map.class, body);

        Map<String, Object> responseBody = recaptchaResponseEntity.getBody();
        boolean recaptchaSucess = (Boolean) responseBody.get("success");
        if (!recaptchaSucess) {
            List<String> errorCodes = (List) responseBody.get("error-codes");
            return errorCodes.stream().map(RecaptchaUtil.RECAPTCHA_ERROR_CODE::get).collect(Collectors.joining(", "));
        } else return StringUtils.EMPTY;
    }
}
