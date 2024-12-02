package com.group.pet_service.service;

import com.group.pet_service.dto.job_position.JobPositionEditRequest;
import com.group.pet_service.dto.job_position.JobPositionRequest;
import com.group.pet_service.dto.job_position.JobPositionResponse;
import com.group.pet_service.exception.AppException;
import com.group.pet_service.mapper.JobPositionMapper;
import com.group.pet_service.model.JobPosition;
import com.group.pet_service.repository.JobPositionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class JobPositionService {

    private final JobPositionRepository jobPositionRepository;
    private final JobPositionMapper jobPositionMapper;

    public void addJobPosition(JobPositionRequest request) {
        JobPosition jobPosition = JobPosition.builder()
                .name(request.getName())
                .build();
        jobPositionRepository.save(jobPosition);
    }

    public JobPositionEditRequest findById(String id) {
        JobPosition jobPosition = jobPositionRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Job position not found")
        );
        return jobPositionMapper.toJobPositionEditRequest(jobPosition);
    }

    public List<JobPositionResponse> findAll() {
        List<JobPosition> jobPositions = jobPositionRepository.findAll();
        return jobPositionMapper.toListJobPositionResponse(jobPositions);
    }

    public void update(String id, JobPositionEditRequest request) {
        JobPosition jobPosition = jobPositionRepository.findById(id).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Job position not found")
        );
        jobPosition.setName(request.getName());
    }

    public void delete(String id) {
        try {
            jobPositionRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new AppException("Cannot delete species: It is being referenced by other records.");
        } catch (Exception e) {
            throw new AppException("An unexpected error occurred while deleting species.");
        }
    }
}
