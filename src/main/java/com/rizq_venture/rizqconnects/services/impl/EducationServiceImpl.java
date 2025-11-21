package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.request.EducationRequest;
import com.rizq_venture.rizqconnects.dto.response.EducationResponse;
import com.rizq_venture.rizqconnects.exception.ResourceNotFoundException;
import com.rizq_venture.rizqconnects.model.Education;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.EducationRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.EducationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationServiceImpl implements EducationService {

    private final EducationRepo educationRepo;
    private final UserRepo userRepo;

    @Override
    public EducationResponse addEducation(EducationRequest request, Long userId) {

        Users user=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("ID not FOUND"));

        Education education=Education.builder()
                .user(user)
                .schoolName(request.getSchoolName())
                .degree(request.getDegree())
                .fieldOfStudy(request.getFieldOfStudy())
                .grade(request.getGrade())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        education=educationRepo.save(education);
        return buildEducationResponse(education);
    }

    @Transactional(readOnly = true)
    public List<EducationResponse> getUserEducation(Long userId) {
        return educationRepo.findByUserUserIdOrderByStartDateDesc(userId)
                .stream()
                .map(this::buildEducationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EducationResponse updateEducation(Long userId, Long educationId, EducationRequest request) {
        Education education = educationRepo.findByEducationIdAndUserUserId(educationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));

        education.setSchoolName(request.getSchoolName());
        education.setDegree(request.getDegree());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setStartDate(request.getStartDate());
        education.setEndDate(request.getEndDate());
        education.setGrade(request.getGrade());

        education = educationRepo.save(education);
        return buildEducationResponse(education);
    }

    @Transactional
    public void deleteEducation(Long userId, Long educationId) {
        Education education = educationRepo.findByEducationIdAndUserUserId(educationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));
        educationRepo.delete(education);
    }

    private EducationResponse buildEducationResponse(Education education) {

        return EducationResponse.builder()
                .educationId(education.getEducationId())
                .schoolName(education.getSchoolName())
                .degree(education.getDegree())
                .fieldOfStudy(education.getFieldOfStudy())
                .grade(education.getGrade())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .build();
    }

}
