package ra.edu.service;

import ra.edu.dto.response.LessonResponse;

public interface LessonService {
    LessonResponse getLessonDetail(Integer lessonId);
}
