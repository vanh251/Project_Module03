package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.dto.request.CourseRequest;
import ra.edu.dto.request.CourseStatusRequest;
import ra.edu.dto.response.CourseDetailResponse;
import ra.edu.dto.response.CourseResponse;
import ra.edu.dto.response.LessonResponse;
import ra.edu.entity.CourseStatus;

import java.util.List;

public interface CourseService {
    public Page<CourseResponse> getAllCourses(CourseStatus status, int page, int size);
    public CourseDetailResponse getCourseDetail(Integer courseId);
    public CourseResponse createCourse(CourseRequest request);
    public CourseResponse updateCourse(Integer courseId, CourseRequest request);
    public CourseResponse updateCourseStatus(Integer courseId, CourseStatusRequest request);
    public void deleteCourse(Integer courseId);
    public List<LessonResponse> getCourseLessons(Integer courseId);
}
