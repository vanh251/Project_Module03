package ra.edu.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.dto.response.TopCourseResponse;
import ra.edu.entity.Enrollment;
import ra.edu.entity.User;

import ra.edu.entity.Course;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByStudent(User student);
    boolean existsByStudentAndCourse(User student, Course course);

    @Query("SELECT new ra.edu.dto.response.TopCourseResponse(" +
           "c.courseId, c.title, c.teacher.fullName, COUNT(e.enrollmentId)) " +
           "FROM Enrollment e JOIN e.course c " +
           "GROUP BY c.courseId, c.title, c.teacher.fullName " +
           "ORDER BY COUNT(e.enrollmentId) DESC")
    List<TopCourseResponse> findTopCoursesByEnrollment(Pageable pageable);
}
