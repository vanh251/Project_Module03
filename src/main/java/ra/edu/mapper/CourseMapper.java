package ra.edu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ra.edu.dto.response.CourseDetailResponse;
import ra.edu.dto.response.CourseResponse;
import ra.edu.dto.response.EnrollmentResponse;
import ra.edu.dto.response.LessonResponse;
import ra.edu.entity.Course;
import ra.edu.entity.Enrollment;
import ra.edu.entity.Lesson;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(source = "teacher.fullName", target = "teacherName")
    @Mapping(source = "status", target = "status")
    CourseResponse toCourseResponse(Course course);

    @Mapping(source = "teacher.fullName", target = "teacherName")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "lessons", ignore = true)
    CourseDetailResponse toCourseDetailResponse(Course course);

    LessonResponse toLessonResponse(Lesson lesson);

    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "course.title", target = "courseTitle")
    @Mapping(source = "course.teacher.fullName", target = "teacherName")
    EnrollmentResponse toEnrollmentResponse(Enrollment enrollment);
}
