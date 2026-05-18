package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.config.exception.BadRequestException;
import ra.edu.config.exception.ForbiddenOperationException;
import ra.edu.config.exception.ResourceNotFoundException;
import ra.edu.dto.response.CourseOverviewDetail;
import ra.edu.dto.response.CourseProgressDetail;
import ra.edu.dto.response.StudentProgressReportResponse;
import ra.edu.dto.response.TeacherCoursesOverviewResponse;
import ra.edu.dto.response.TopCourseResponse;
import ra.edu.entity.Course;
import ra.edu.entity.CourseStatus;
import ra.edu.entity.Enrollment;
import ra.edu.entity.EnrollmentStatus;
import ra.edu.entity.Role;
import ra.edu.entity.User;
import ra.edu.mapper.ReportMapper;
import ra.edu.repository.CourseRepository;
import ra.edu.repository.EnrollmentRepository;
import ra.edu.repository.ReviewRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.ReportService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final ReportMapper reportMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TopCourseResponse> getTopCourses(int limit) {
        return enrollmentRepository.findTopCoursesByEnrollment(PageRequest.of(0, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentProgressReportResponse getStudentProgressReport(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));

        if (student.getRole() != Role.STUDENT) {
            throw new ForbiddenOperationException("Người dùng này không phải là sinh viên");
        }

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

        if (enrollments.isEmpty()) {
            return StudentProgressReportResponse.builder()
                    .studentId(student.getUserId())
                    .studentName(student.getFullName())
                    .totalEnrolledCourses(0)
                    .totalCompletedCourses(0)
                    .averageProgress(BigDecimal.ZERO)
                    .courseDetails(List.of())
                    .build();
        }

        List<CourseProgressDetail> details = reportMapper.toCourseProgressDetailList(enrollments);

        int totalEnrolled = enrollments.size();
        int totalCompleted = 0;
        BigDecimal totalProgress = BigDecimal.ZERO;

        for (Enrollment e : enrollments) {
            if (e.getStatus() == EnrollmentStatus.COMPLETED) {
                totalCompleted++;
            }
            totalProgress = totalProgress.add(e.getProgressPercentage());
        }

        BigDecimal averageProgress = totalProgress.divide(
                BigDecimal.valueOf(totalEnrolled), 2, RoundingMode.HALF_UP
        );

        return StudentProgressReportResponse.builder()
                .studentId(student.getUserId())
                .studentName(student.getFullName())
                .totalEnrolledCourses(totalEnrolled)
                .totalCompletedCourses(totalCompleted)
                .averageProgress(averageProgress)
                .courseDetails(details)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherCoursesOverviewResponse getTeacherCoursesOverview(Integer teacherId) {
        User teacher = userRepository.findById(teacherId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException("Người dùng được chỉ định không phải là giảng viên");
        }

        List<Course> courses = courseRepository.findAllByTeacherId(teacherId);

        long totalStudents = 0;
        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalPublished = 0;
        List<CourseOverviewDetail> courseDetails = new ArrayList<>();

        for (Course course : courses) {
            long studentCount = course.getEnrollments() != null ? course.getEnrollments().size() : 0;
            totalStudents += studentCount;

            // Doanh thu = giá * số lượt đăng ký
            totalRevenue = totalRevenue.add(course.getPrice().multiply(BigDecimal.valueOf(studentCount)));

            if (course.getStatus() == CourseStatus.PUBLISHED) {
                totalPublished++;
            }

            // Tính rating trung bình từng khóa
            double courseAvgRating = reviewRepository.findAverageRatingByCourseId(course.getCourseId());

            courseDetails.add(CourseOverviewDetail.builder()
                    .courseId(course.getCourseId())
                    .title(course.getTitle())
                    .status(course.getStatus().name())
                    .price(course.getPrice())
                    .studentCount(studentCount)
                    .averageRating(courseAvgRating)
                    .build());
        }

        // Rating trung bình tổng thể của giảng viên
        double overallAvgRating = reviewRepository.findAverageRatingByTeacherId(teacherId);

        return TeacherCoursesOverviewResponse.builder()
                .teacherId(teacher.getUserId().intValue())
                .teacherName(teacher.getFullName())
                .totalCourses(courses.size())
                .totalPublishedCourses(totalPublished)
                .totalStudentsEnrolled(totalStudents)
                .totalRevenue(totalRevenue)
                .averageRating(overallAvgRating)
                .courseDetails(courseDetails)
                .build();
    }
}
