package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.config.exception.ForbiddenOperationException;
import ra.edu.config.exception.ResourceNotFoundException;
import ra.edu.dto.request.EnrollmentRequest;
import ra.edu.dto.response.EnrollmentResponse;
import ra.edu.entity.Course;
import ra.edu.entity.CourseStatus;
import ra.edu.entity.Enrollment;
import ra.edu.entity.EnrollmentStatus;
import ra.edu.entity.Lesson;
import ra.edu.entity.LessonProgress;
import ra.edu.entity.User;
import ra.edu.mapper.CourseMapper;
import ra.edu.repository.CourseRepository;
import ra.edu.repository.EnrollmentRepository;
import ra.edu.repository.LessonProgressRepository;
import ra.edu.repository.LessonRepository;
import ra.edu.service.EnrollmentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getMyEnrollments(User student) {
        return enrollmentRepository.findByStudent(student).stream()
                .map(courseMapper::toEnrollmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EnrollmentResponse enrollCourse(EnrollmentRequest request, User student) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw new ForbiddenOperationException("Bạn chỉ có thể đăng ký khóa học đã xuất bản");
        }

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new ForbiddenOperationException("Bạn đã đăng ký khóa học này rồi");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return courseMapper.toEnrollmentResponse(savedEnrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollmentDetail(Integer enrollmentId, User student) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin đăng ký"));

        if (!enrollment.getStudent().getUserId().equals(student.getUserId())) {
            throw new ForbiddenOperationException("Bạn không có quyền xem thông tin đăng ký của người khác");
        }

        return courseMapper.toEnrollmentResponse(enrollment);
    }

    @Override
    @Transactional
    public EnrollmentResponse completeLesson(Integer enrollmentId, Integer lessonId, User student) {
        // 1. Lấy thông tin đăng ký
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin đăng ký"));

        if (!enrollment.getStudent().getUserId().equals(student.getUserId())) {
            throw new ForbiddenOperationException("Bạn không có quyền cập nhật tiến độ của người khác");
        }

        // 2. Lấy thông tin bài học
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài học"));

        if (!lesson.getCourse().getCourseId().equals(enrollment.getCourse().getCourseId())) {
            throw new ForbiddenOperationException("Bài học này không thuộc khóa học bạn đang đăng ký");
        }

        if (!lesson.getIsPublished()) {
            throw new ForbiddenOperationException("Bài học này chưa được xuất bản");
        }

        // 3. Cập nhật tiến độ bài học (LessonProgress)
        LessonProgress progress = lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson)
                .orElse(LessonProgress.builder()
                        .enrollment(enrollment)
                        .lesson(lesson)
                        .build());

        if (progress.getIsCompleted()) {
            throw new ForbiddenOperationException("Bài học này đã được hoàn thành từ trước");
        }

        progress.setIsCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        lessonProgressRepository.save(progress);

        // 4. Tính toán lại phần trăm hoàn thành khóa học (Enrollment)
        Course course = enrollment.getCourse();
        long totalPublishedLessons = course.getLessons().stream().filter(Lesson::getIsPublished).count();
        long completedLessons = lessonProgressRepository.countByEnrollmentAndIsCompletedTrue(enrollment);

        BigDecimal progressPercentage = BigDecimal.ZERO;
        if (totalPublishedLessons > 0) {
            progressPercentage = BigDecimal.valueOf((double) completedLessons / totalPublishedLessons * 100);
        }

        enrollment.setProgressPercentage(progressPercentage);

        if (progressPercentage.compareTo(BigDecimal.valueOf(100)) >= 0) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            enrollment.setCompletionDate(LocalDateTime.now());
        }

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return courseMapper.toEnrollmentResponse(savedEnrollment);
    }
}
