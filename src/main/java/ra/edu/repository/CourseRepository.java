package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Course;
import ra.edu.entity.CourseStatus;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT c FROM Course c Where (:status IS NULL OR c.status = :status)")
     Page<Course> findAllByStatus(@Param("status")CourseStatus status, Pageable pageable);
}
