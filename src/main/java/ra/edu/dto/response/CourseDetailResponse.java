package ra.edu.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailResponse {
    private Integer courseId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer durationHours;
    private String teacherName;
    private String status;
    private List<LessonResponse> lessons;
}
