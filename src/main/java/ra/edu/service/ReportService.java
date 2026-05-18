package ra.edu.service;

import ra.edu.dto.response.StudentProgressReportResponse;
import ra.edu.dto.response.TeacherCoursesOverviewResponse;
import ra.edu.dto.response.TopCourseResponse;

import java.util.List;

public interface ReportService {
    List<TopCourseResponse> getTopCourses(int limit);
    StudentProgressReportResponse getStudentProgressReport(Long studentId);
    TeacherCoursesOverviewResponse getTeacherCoursesOverview(Integer teacherId);
}
