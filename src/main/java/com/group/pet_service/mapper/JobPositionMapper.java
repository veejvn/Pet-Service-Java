package com.group.pet_service.mapper;

import com.group.pet_service.dto.job_position.JobPositionEditRequest;
import com.group.pet_service.dto.job_position.JobPositionResponse;
import com.group.pet_service.model.JobPosition;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobPositionMapper {

    JobPositionResponse toJobPositionResponse(JobPosition jobPosition);

    List<JobPositionResponse> toListJobPositionResponse(List<JobPosition> jobPositionResponses);

    JobPositionEditRequest toJobPositionEditRequest(JobPosition jobPosition);
}
