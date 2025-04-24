package site.shresthacyrus.neighborhoodhelpplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JobMapper {

    // Map JobRequestDto → Job entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "seeker", ignore = true)
    @Mapping(target = "bids", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "acceptedBid", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(source = "skillId", target = "skill.id")
    Job jobRequestDtoToJob(JobRequestDto jobRequestDto);

    @Mapping(source = "publicId", target = "publicId")
    @Mapping(source = "skill.name", target = "skillName")
    @Mapping(source = "seeker.legalFullName", target = "seekerFullName")
    JobResponseDto jobToJobResponseDto(Job job);

    List<JobResponseDto> jobsToJobResponseDtoList(List<Job> jobs);
}
