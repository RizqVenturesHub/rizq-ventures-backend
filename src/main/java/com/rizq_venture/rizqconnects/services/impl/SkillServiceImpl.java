package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.request.SkillRequest;
import com.rizq_venture.rizqconnects.dto.response.SkillResponse;
import com.rizq_venture.rizqconnects.exception.BadRequestException;
import com.rizq_venture.rizqconnects.exception.ResourceNotFoundException;
import com.rizq_venture.rizqconnects.model.Skill;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.SkillRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillServiceImpl implements SkillService {

    private final SkillRepo skillRepo;
    private final UserRepo userRepo;

    @Transactional
    public SkillResponse addSkill(Long userId, SkillRequest request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (skillRepo.existsByUserUserIdAndSkillNameIgnoreCase(userId, request.getSkillName())) {
            throw new ResourceNotFoundException("Skill already exists");
        }
        if (request.getSkillType() == Skill.SkillType.PRIMARY) {
            Long primaryCount = skillRepo.countPrimarySkills(userId);
            if (primaryCount >= 5) {
                throw new BadRequestException("Maximum 5 primary skills allowed. Please set existing skills as secondary or remove some.");
            }
        }

        Skill skill = Skill.builder()
                .user(user)
                .skillName(request.getSkillName())
                .skillType(request.getSkillType())
                .proficiencyLevel(request.getProficiencyLevel())
                .endorsementsCount(0)
                .build();

        skill = skillRepo.save(skill);
        log.info("Skill added for user {}: {} ({})", userId, skill.getSkillName(), skill.getSkillType());

        return buildSkillResponse(skill);
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getUserSkills(Long userId) {
        return skillRepo.findByUserUserIdOrderBySkillTypeAscSkillNameAsc(userId)
                .stream()
                .map(this::buildSkillResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getPrimarySkills(Long userId) {
        return skillRepo.findPrimarySkillsByUserId(userId)
                .stream()
                .map(this::buildSkillResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getSecondarySkills(Long userId) {
        return skillRepo.findSecondarySkillsByUserId(userId)
                .stream()
                .map(this::buildSkillResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SkillResponse updateSkill(Long userId, Long skillId, SkillRequest request) {
        Skill skill = skillRepo.findBySkillIdAndUserUserId(skillId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        if (request.getSkillType() == Skill.SkillType.PRIMARY &&
                skill.getSkillType() != Skill.SkillType.PRIMARY) {
            Long primaryCount = skillRepo.countPrimarySkills(userId);
            if (primaryCount >= 5) {
                throw new BadRequestException("Maximum 5 primary skills allowed");
            }
        }

        skill.setSkillName(request.getSkillName());
        skill.setSkillType(request.getSkillType());
        skill.setProficiencyLevel(request.getProficiencyLevel());

        skill = skillRepo.save(skill);
        log.info("Skill updated for user {}: {} ({})", userId, skill.getSkillName(), skill.getSkillType());

        return buildSkillResponse(skill);
    }

    @Transactional
    public void deleteSkill(Long userId, Long skillId) {
        Skill skill = skillRepo.findBySkillIdAndUserUserId(skillId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        skillRepo.delete(skill);
        log.info("Skill deleted: {} for user {}", skillId, userId);
    }

    private SkillResponse buildSkillResponse(Skill skill) {
        return SkillResponse.builder()
                .skillId(skill.getSkillId())
                .skillName(skill.getSkillName())
                .skillType(skill.getSkillType())
                .proficiencyLevel(skill.getProficiencyLevel())
                .endorsementsCount(skill.getEndorsementsCount())
                .createdAt(skill.getCreatedAt())
                .build();
    }
}
