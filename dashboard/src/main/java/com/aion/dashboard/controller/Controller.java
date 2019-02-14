package com.aion.dashboard.controller;

import com.aion.dashboard.exceptions.ApiInvalidRequestException;
import com.aion.dashboard.services.FeedbackService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private FeedbackService feedbackService;

    private final Logger general = LoggerFactory.getLogger("general");

    @PostMapping(path = "/sendFeedback")
    public String sendFeedback(@RequestParam(value = "topic") String topic,
                               @RequestParam(value = "message") String message) {
        try {
            if(topic == null || topic.trim().isEmpty() || message == null || message.trim().isEmpty()) {
                throw new ApiInvalidRequestException("Invalid Request: Cannot have empty/null parameters");
            } else {
                return feedbackService.storeFeedback(topic, message);
            }
        } catch(Exception e) {
            general.debug("sendFeedback", e);
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }
}
