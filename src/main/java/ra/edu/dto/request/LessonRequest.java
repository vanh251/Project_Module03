package ra.edu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    private String contentUrl;
    
    private String textContent;

    @NotNull(message = "Thứ tự bài học không được để trống")
    @Min(value = 1, message = "Thứ tự bài học phải lớn hơn 0")
    private Integer orderIndex;

    @Builder.Default
    private Boolean isPublished = false;
}
