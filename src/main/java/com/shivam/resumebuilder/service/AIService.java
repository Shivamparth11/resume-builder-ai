package com.shivam.resumebuilder.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {
    private final RestTemplate restTemplate;

    public AIService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    public String getAIFeedback(String summary){
        String pythonApiUrl = "http://localhost:5000/analyze";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String , String> payload = new HashMap<>();
        payload.put("summary", summary);

        HttpEntity<Map<String , String >> entity = new HttpEntity<>(payload , headers);

        try {
            return restTemplate.postForObject(pythonApiUrl , payload , String.class);
        }catch (Exception e){
            return "⚠ Error contacting AI service: "+ e.getMessage();
        }
    }
}