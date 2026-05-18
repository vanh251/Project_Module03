package ra.edu.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherCoursesOverviewResponse {
    private Integer teacherId;
    private String teacherName;

    // Các chỉ số thống kê tổng quan của giảng viên
    private int totalCourses;
    private int totalPublishedCourses;
    private long totalStudentsEnrolled;
    private BigDecimal totalRevenue;
    private double averageRating;

    // Danh sách chi tiết từng khóa học
    private List<CourseOverviewDetail> courseDetails;
}
