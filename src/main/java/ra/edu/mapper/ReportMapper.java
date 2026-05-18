package ra.edu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ra.edu.dto.response.CourseProgressDetail;
import ra.edu.entity.Enrollment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "course.title", target = "courseTitle")
    @Mapping(source = "progressPercentage", target = "progress", qualifiedByName = "toIntProgress")
    CourseProgressDetail toCourseProgressDetail(Enrollment enrollment);

    List<CourseProgressDetail> toCourseProgressDetailList(List<Enrollment> enrollments);

    // Converter: làm tròn xuống, BigDecimal -> Integer (0-100)
    @Named("toIntProgress")
    default Integer toIntProgress(BigDecimal value) {
        if (value == null) return 0;
        return value.setScale(0, RoundingMode.HALF_UP).intValue();
    }
}
