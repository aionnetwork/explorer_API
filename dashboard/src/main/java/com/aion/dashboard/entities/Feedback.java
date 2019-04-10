package com.aion.dashboard.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue()
    private Long id;
    private String topic;
    private String message;
    private Long timestamp;

    public Long getId() {
        return id;
    }
    public String getTopic() {
        return topic;
    }
    public String getMessage() {
        return message;
    }
    public Long getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Feedback(String topic, String message) {
        this.topic = topic;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return getId() == feedback.getId() &&
                Objects.equals(getTopic(), feedback.topic) &&
                Objects.equals(getMessage(), feedback.message) &&
                getTimestamp() == feedback.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTopic(), getMessage(), getTimestamp());
    }
}
