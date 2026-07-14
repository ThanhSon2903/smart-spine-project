package DATN.example.demo.mapper;

import DATN.example.demo.dto.response.PostureResponse;
import DATN.example.demo.entity.PostureSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostureSnapshotMapper {
    PostureResponse toPostureResponse(PostureSnapshot postureSnapshot);
}
