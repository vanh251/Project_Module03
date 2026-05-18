package ra.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopCourseResponse {
    private Integer courseId;
    private String title;
    private String teacherName;
    private Long enrollmentCount;
}
