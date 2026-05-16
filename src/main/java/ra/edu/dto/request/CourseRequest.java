package ra.edu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {
    @NotBlank(message = "Tiêu đề khóa học không được để trống")
    private String title;

    private String description;

    @NotNull(message = "ID giảng viên không được để trống")
    private Long teacherId;

    @NotNull(message = "Giá khóa học không được để trống")
    @Min(value = 0, message = "Giá khóa học không được âm")
    private BigDecimal price;

    @Min(value = 1, message = "Thời lượng khóa học phải lớn hơn 0")
    private Integer durationHours;
}
