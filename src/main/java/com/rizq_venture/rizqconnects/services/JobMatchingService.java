package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.model.Job;

import java.util.List;

public interface JobMatchingService {

    public void notifyMatchingUsers(Job job);
    public void createJobMatchNotification(Long userId, Job job);
    public Double calculateSkillMatchPercentage(Long userId, Long jobId);
    public List<Job> getRecommendedJobs(Long userId, int limit);
}
