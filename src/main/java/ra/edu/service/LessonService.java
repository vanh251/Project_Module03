package ra.edu.service;

import ra.edu.dto.request.LessonPublishRequest;
import ra.edu.dto.request.LessonRequest;
import ra.edu.dto.response.LessonResponse;
import ra.edu.entity.User;

public interface LessonService {
    LessonResponse getLessonDetail(Integer lessonId);
    LessonResponse updateLesson(Integer lessonId, LessonRequest request, User currentUser);
    LessonResponse updateLessonStatus(Integer lessonId, LessonPublishRequest request, User currentUser);
    void deleteLesson(Integer lessonId, User currentUser);
}
