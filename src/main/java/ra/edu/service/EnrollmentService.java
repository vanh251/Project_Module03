package ra.edu.service;

import ra.edu.dto.request.EnrollmentRequest;
import ra.edu.dto.response.EnrollmentResponse;
import ra.edu.entity.User;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentResponse> getMyEnrollments(User student);
    EnrollmentResponse enrollCourse(EnrollmentRequest request, User student);
    EnrollmentResponse getEnrollmentDetail(Integer enrollmentId, User student);
    EnrollmentResponse completeLesson(Integer enrollmentId, Integer lessonId, User student);
}
