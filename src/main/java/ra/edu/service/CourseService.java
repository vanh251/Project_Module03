package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.dto.request.CourseRequest;
import ra.edu.dto.request.CourseStatusRequest;
import ra.edu.dto.request.LessonRequest;
import ra.edu.dto.request.ReviewRequest;
import ra.edu.dto.response.CourseDetailResponse;
import ra.edu.dto.response.CourseResponse;
import ra.edu.dto.response.LessonResponse;
import ra.edu.dto.response.ReviewResponse;
import ra.edu.entity.CourseStatus;
import ra.edu.entity.User;

import java.util.List;

public interface CourseService {
    Page<CourseResponse> getAllCourses(CourseStatus status, int page, int size);

    Page<CourseResponse> getCoursesByStatus(CourseStatus status, int page, int size, User currentUser);

    Page<CourseResponse> searchCourses(String keyword, int page, int size);

    Page<CourseResponse> getCoursesByTeacher(Integer teacherId, int page, int size);

    public CourseDetailResponse getCourseDetail(Integer courseId);

    public CourseResponse createCourse(CourseRequest request);

    public CourseResponse updateCourse(Integer courseId, CourseRequest request);

    public CourseResponse updateCourseStatus(Integer courseId, CourseStatusRequest request);

    public void deleteCourse(Integer courseId);

    public List<LessonResponse> getCourseLessons(Integer courseId);

    public LessonResponse addLesson(Integer courseId, LessonRequest request, User currentUser);

    Page<ReviewResponse> getCourseReviews(Integer courseId, int page, int size);

    ReviewResponse addCourseReview(Integer courseId, ReviewRequest request, User currentUser);
}
