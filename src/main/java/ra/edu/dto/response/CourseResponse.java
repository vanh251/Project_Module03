package ra.edu.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private Integer courseId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer durationHours;
    private String teacherName;
    private String status;
}