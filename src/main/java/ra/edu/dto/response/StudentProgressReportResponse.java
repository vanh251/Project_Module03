package ra.edu.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentProgressReportResponse {
    private Long studentId;
    private String studentName;
    private int totalEnrolledCourses;
    private int totalCompletedCourses;
    private BigDecimal averageProgress; // Tiến độ trung bình (%)
    // Danh sách chi tiết từng khóa
    private List<CourseProgressDetail> courseDetails;
}