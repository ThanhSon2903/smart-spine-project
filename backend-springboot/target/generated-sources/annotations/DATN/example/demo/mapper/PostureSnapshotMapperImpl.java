package DATN.example.demo.mapper;

import DATN.example.demo.dto.response.PostureResponse;
import DATN.example.demo.entity.PostureSnapshot;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-07T13:44:17+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class PostureSnapshotMapperImpl implements PostureSnapshotMapper {

    @Override
    public PostureResponse toPostureResponse(PostureSnapshot postureSnapshot) {
        if ( postureSnapshot == null ) {
            return null;
        }

        PostureResponse.PostureResponseBuilder postureResponse = PostureResponse.builder();

        postureResponse.postureSnapshotId( postureSnapshot.getPostureSnapshotId() );
        postureResponse.shouterRatio( postureSnapshot.getShouterRatio() );
        postureResponse.torsoAngle( postureSnapshot.getTorsoAngle() );
        postureResponse.neckAngle( postureSnapshot.getNeckAngle() );
        postureResponse.status( postureSnapshot.getStatus() );
        postureResponse.createdAt( postureSnapshot.getCreatedAt() );

        return postureResponse.build();
    }
}
