package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.Feedback;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface FeedbackRepository extends CrudRepository<Feedback, Long> {
}
