package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListEndedCourseByInternResponse {
    List<EndedCourseByInternResponse> endedCourseByInternResponse;
}
