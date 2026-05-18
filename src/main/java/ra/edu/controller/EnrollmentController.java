package ra.edu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import ra.edu.dto.request.EnrollmentRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.EnrollmentResponse;
import ra.edu.entity.User;
import ra.edu.service.EnrollmentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @GetMapping
    public ApiResponse<List<EnrollmentResponse>> getMyEnrollments(@AuthenticationPrincipal User currentUser) {
        List<EnrollmentResponse> enrollments = enrollmentService.getMyEnrollments(currentUser);
        return ApiResponse.success("Danh sách khóa học đã đăng ký", enrollments);
    }

    @PostMapping
    public ApiResponse<EnrollmentResponse> enrollCourse(
            @Valid @RequestBody EnrollmentRequest request,
            @AuthenticationPrincipal User currentUser) {
        EnrollmentResponse response = enrollmentService.enrollCourse(request, currentUser);
        return ApiResponse.success("Đăng ký khóa học thành công", response);
    }

    @GetMapping("/{enrollment_id}")
    public ApiResponse<EnrollmentResponse> getEnrollmentDetail(
            @PathVariable("enrollment_id") Integer enrollmentId,
            @AuthenticationPrincipal User currentUser) {
        EnrollmentResponse response = enrollmentService.getEnrollmentDetail(enrollmentId, currentUser);
        return ApiResponse.success("Chi tiết đăng ký khóa học", response);
    }

    @PutMapping("/{enrollment_id}/complete_lesson/{lesson_id}")
    public ApiResponse<EnrollmentResponse> completeLesson(
            @PathVariable("enrollment_id") Integer enrollmentId,
            @PathVariable("lesson_id") Integer lessonId,
            @AuthenticationPrincipal User currentUser) {
        EnrollmentResponse response = enrollmentService.completeLesson(enrollmentId, lessonId, currentUser);
        return ApiResponse.success("Đánh dấu hoàn thành bài học thành công", response);
    }
}
