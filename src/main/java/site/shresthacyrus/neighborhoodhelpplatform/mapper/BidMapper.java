package site.shresthacyrus.neighborhoodhelpplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid.BidResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.model.Bid;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BidMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "helper", ignore = true)
    @Mapping(source = "jobPublicId", target = "job.publicId")
    Bid bidRequestDtoToBid(BidRequestDto bidRequestDto);

    @Mapping(source = "job.publicId", target = "jobPublicId")
    @Mapping(source = "helper.username", target = "helperUsername")
    @Mapping(source = "helper.legalFullName", target = "helperFullName")
    BidResponseDto bidToBidResponseDto(Bid bid);

    List<BidResponseDto> bidToBidResponseDtoList(List<Bid> bids);

}
