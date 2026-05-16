package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonPublishRequest {
    @NotNull(message = "Trạng thái xuất bản không được để trống")
    private Boolean isPublished;
}
