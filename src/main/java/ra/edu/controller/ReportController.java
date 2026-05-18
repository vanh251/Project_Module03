package ra.edu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.StudentProgressReportResponse;
import ra.edu.dto.response.TeacherCoursesOverviewResponse;
import ra.edu.dto.response.TopCourseResponse;
import ra.edu.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/top_courses")
    public ApiResponse<List<TopCourseResponse>> getTopCourses(
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<TopCourseResponse> result = reportService.getTopCourses(limit);
        return ApiResponse.success("Danh sách khóa học phổ biến nhất", result);
    }

    @GetMapping("/student_progress/{studentId}")
    public ApiResponse<StudentProgressReportResponse> getStudentProgressReport(
            @PathVariable Long studentId
    ) {
        StudentProgressReportResponse result = reportService.getStudentProgressReport(studentId);
        return ApiResponse.success("Báo cáo tiến độ học tập của sinh viên", result);
    }

    @GetMapping("/teacher_courses_overview/{teacher_id}")
    public ApiResponse<TeacherCoursesOverviewResponse> getTeacherCoursesOverview(
            @PathVariable("teacher_id") Integer teacherId
    ) {
        TeacherCoursesOverviewResponse result = reportService.getTeacherCoursesOverview(teacherId);
        return ApiResponse.success("Tổng quan khóa học của giảng viên", result);
    }
}
