package ra.edu.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseOverviewDetail {
    private Integer courseId;
    private String title;
    private String status;
    private BigDecimal price;
    private long studentCount;
    private double averageRating;
}