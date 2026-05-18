package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Course;
import ra.edu.entity.Review;
import ra.edu.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.course.courseId = :courseId")
    double findAverageRatingByCourseId(@Param("courseId") Integer courseId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.course.teacher.userId = :teacherId")
    double findAverageRatingByTeacherId(@Param("teacherId") Integer teacherId);

    boolean existsByStudentAndCourse(User student, Course course);

    @Query("SELECT r FROM Review r WHERE r.course.courseId = :courseId ORDER BY r.createdAt DESC")
    Page<Review> findByCourseId(@Param("courseId") Integer courseId, Pageable pageable);
}
