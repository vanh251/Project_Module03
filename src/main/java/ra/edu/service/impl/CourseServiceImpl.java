package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.config.exception.ForbiddenOperationException;
import ra.edu.config.exception.ResourceNotFoundException;
import ra.edu.dto.request.CourseRequest;
import ra.edu.dto.response.CourseDetailResponse;
import ra.edu.dto.response.CourseResponse;
import ra.edu.dto.response.LessonResponse;
import ra.edu.entity.Course;
import ra.edu.entity.CourseStatus;
import ra.edu.entity.Lesson;
import ra.edu.entity.Role;
import ra.edu.entity.User;
import ra.edu.mapper.CourseMapper;
import ra.edu.repository.CourseRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.CourseService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> getAllCourses(CourseStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Course> courses = courseRepository.findAllByStatus(status, pageable);
        return courses.map(courseMapper::toCourseResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseDetail(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        CourseDetailResponse response = courseMapper.toCourseDetailResponse(course);

        List<LessonResponse> publishedLessons = course.getLessons().stream()
                .filter(Lesson::getIsPublished)
                .map(courseMapper::toLessonResponse)
                .collect(Collectors.toList());

        response.setLessons(publishedLessons);
        return response;
    }

    @Override
    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new ForbiddenOperationException("Người dùng được chỉ định không phải là giảng viên");
        }

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacher(teacher)
                .price(request.getPrice())
                .durationHours(request.getDurationHours())
                .status(CourseStatus.DRAFT)
                .build();

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toCourseResponse(savedCourse);
    }
    @Override
    @Transactional
    public CourseResponse updateCourse(Integer courseId, CourseRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        // Cập nhật giáo viên nếu có thay đổi
        User newTeacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));

        if (newTeacher.getRole() != Role.TEACHER) {
            throw new ForbiddenOperationException("Người dùng được chỉ định không phải là giảng viên");
        }
        course.setTeacher(newTeacher);

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setDurationHours(request.getDurationHours());

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toCourseResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourseStatus(Integer courseId, ra.edu.dto.request.CourseStatusRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        course.setStatus(request.getStatus());

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toCourseResponse(savedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        if (course.getEnrollments() != null && !course.getEnrollments().isEmpty()) {
            throw new ForbiddenOperationException("Không thể xóa khóa học đã có học viên đăng ký");
        }

        courseRepository.delete(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getCourseLessons(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        return course.getLessons().stream()
                .filter(Lesson::getIsPublished)
                .map(courseMapper::toLessonResponse)
                .collect(Collectors.toList());
    }
}
