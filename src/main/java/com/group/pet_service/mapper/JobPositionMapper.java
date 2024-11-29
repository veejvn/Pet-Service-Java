package com.group.pet_service.mapper;

import com.group.pet_service.dto.admin.JobPositionResponse;
import com.group.pet_service.model.JobPosition;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobPositionMapper {

    JobPositionResponse toJobPositionResponse(JobPosition jobPosition);

    List<JobPositionResponse> toListJobPositionResponse(List<JobPosition> jobPositionResponses);
}
