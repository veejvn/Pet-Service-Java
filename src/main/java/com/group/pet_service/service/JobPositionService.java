package com.group.pet_service.service;

import com.group.pet_service.dto.admin.JobPositionRequest;
import com.group.pet_service.dto.admin.JobPositionResponse;
import com.group.pet_service.mapper.JobPositionMapper;
import com.group.pet_service.model.JobPosition;
import com.group.pet_service.repository.JobPositionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class JobPositionService {

    private final JobPositionRepository jobPositionRepository;
    private final JobPositionMapper jobPositionMapper;

    public void saveJobPosition(JobPositionRequest request) {
        JobPosition jobPosition = JobPosition.builder()
                .name(request.getName())
                .build();
        jobPositionRepository.save(jobPosition);
    }

    public List<JobPositionResponse> findAll() {
        List<JobPosition> jobPositions = jobPositionRepository.findAll();
        return jobPositionMapper.toListJobPositionResponse(jobPositions);
    }
}
