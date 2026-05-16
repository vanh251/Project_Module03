package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.edu.entity.CourseStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStatusRequest {
    @NotNull(message = "Trạng thái không được để trống")
    private CourseStatus status;
}
