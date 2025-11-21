package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.response.JobResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    public Page<UserResponse> searchUsers
            (String query, String location, Long currentUserId, Pageable pageable);

    public Page<JobResponse> searchJobs
            (String query, String location, Long currentUserId, Pageable pageable);

}
