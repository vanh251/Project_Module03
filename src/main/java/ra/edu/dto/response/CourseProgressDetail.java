package ra.edu.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseProgressDetail {
    private Integer courseId;
    private String courseTitle;
    private String status;
    private Integer progress; // 0 - 100%
}