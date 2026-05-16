package ra.edu.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponse {
    private Integer lessonId;
    private String title;
    private String contentUrl;
    private String textContent;
    private Integer orderIndex;
}
