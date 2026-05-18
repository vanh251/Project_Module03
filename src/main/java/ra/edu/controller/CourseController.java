package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.CourseRequest;
import ra.edu.dto.request.CourseStatusRequest;
import ra.edu.dto.request.LessonRequest;
import ra.edu.dto.response.*;
import ra.edu.entity.CourseStatus;
import ra.edu.entity.User;
import ra.edu.service.CourseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public ApiResponse<PageResponse<CourseResponse>> getAllCourses(
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CourseResponse> coursePage = courseService.getAllCourses(status, page, size);
        return ApiResponse.paginated("Danh sách khóa học: ", coursePage.getContent(), page, size,
                coursePage.getTotalElements());
    }

    @GetMapping(params = "status")
    public ApiResponse<PageResponse<CourseResponse>> getCoursesByStatus(
            @RequestParam("status") CourseStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User currentUser) {
        Page<CourseResponse> coursePage = courseService.getCoursesByStatus(status, page, size, currentUser);
        return ApiResponse.paginated("Danh sách khóa học lọc theo trạng thái: ", coursePage.getContent(), page, size,
                coursePage.getTotalElements());
    }

    @GetMapping(params = "search")
    public ApiResponse<PageResponse<CourseResponse>> searchCourses(
            @RequestParam("search") String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CourseResponse> coursePage = courseService.searchCourses(search, page, size);
        return ApiResponse.paginated("Kết quả tìm kiếm khóa học: ", coursePage.getContent(), page, size,
                coursePage.getTotalElements());
    }

    @GetMapping(params = "teacher_id")
    public ApiResponse<PageResponse<CourseResponse>> getCoursesByTeacher(
            @RequestParam("teacher_id") Integer teacherId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CourseResponse> coursePage = courseService.getCoursesByTeacher(teacherId, page, size);
        return ApiResponse.paginated("Kết quả lọc khóa học theo giảng viên: ", coursePage.getContent(), page, size,
                coursePage.getTotalElements());
    }

    @GetMapping("/{course_id}")
    public ApiResponse<CourseDetailResponse> getCourseDetail(@PathVariable("course_id") Integer courseId) {
        CourseDetailResponse courseDetailResponse = courseService.getCourseDetail(courseId);
        return ApiResponse.success("Chi tiết khóa học", courseDetailResponse);
    }

    @PostMapping
    public ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        CourseResponse response = courseService.createCourse(request);
        return ApiResponse.success("Tạo khóa học thành công", response);
    }

    @PutMapping("/{course_id}")
    public ApiResponse<CourseResponse> updateCourse(
            @PathVariable("course_id") Integer courseId,
            @Valid @RequestBody CourseRequest request) {
        CourseResponse response = courseService.updateCourse(courseId, request);
        return ApiResponse.success("Cập nhật khóa học thành công", response);
    }

    @PutMapping("/{course_id}/status")
    public ApiResponse<CourseResponse> updateCourseStatus(
            @PathVariable("course_id") Integer courseId,
            @Valid @RequestBody CourseStatusRequest request) {
        CourseResponse response = courseService.updateCourseStatus(courseId, request);
        return ApiResponse.success("Cập nhật trạng thái khóa học thành công", response);
    }

    @DeleteMapping("/{course_id}")
    public ApiResponse<Void> deleteCourse(@PathVariable("course_id") Integer courseId) {
        courseService.deleteCourse(courseId);
        return ApiResponse.success("Xóa khóa học thành công", null);
    }

    @GetMapping("/{course_id}/lessons")
    public ApiResponse<List<LessonResponse>> getCourseLessons(@PathVariable("course_id") Integer courseId) {
        List<LessonResponse> lessons = courseService.getCourseLessons(courseId);
        return ApiResponse.success("Danh sách bài học", lessons);
    }

    @PostMapping("/{course_id}/lessons")
    public ApiResponse<LessonResponse> addLesson(
            @PathVariable("course_id") Integer courseId,
            @Valid @RequestBody LessonRequest request,
            @AuthenticationPrincipal User currentUser) {
        LessonResponse response = courseService.addLesson(courseId, request, currentUser);
        return ApiResponse.success("Thêm bài học thành công", response);
    }
}
