package ra.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.edu.entity.EnrollmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponse {
    private Integer enrollmentId;
    private Integer courseId;
    private String courseTitle;
    private String teacherName;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;
    private BigDecimal progressPercentage;
    private LocalDateTime completionDate;
}
