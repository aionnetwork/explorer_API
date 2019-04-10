package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Feedback;
import com.aion.dashboard.repositories.FeedbackRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Cacheable(CacheConfig.STORE_FEEDBACK)
    public String storeFeedback(String topic, String message) {
        // Writing Feedback Message into Database
        feedbackRepository.save(new Feedback(topic, message));

        // Returning Success if Save was Successful
        JSONObject success =  new JSONObject();
        success.put("message", "Successfully Sent Feedback");
        return success.toString();
    }
}

