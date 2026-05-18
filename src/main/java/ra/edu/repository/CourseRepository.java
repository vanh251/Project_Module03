package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Course;
import ra.edu.entity.CourseStatus;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT c FROM Course c WHERE (:status IS NULL OR c.status = :status)")
    Page<Course> findAllByStatus(@Param("status") CourseStatus status, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Course> searchCourses(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.teacher.userId = :teacherId")
    Page<Course> findAllByTeacher(@Param("teacherId") Integer teacherId, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.teacher.userId = :teacherId")
    List<Course> findAllByTeacherId(@Param("teacherId") Integer teacherId);
}
