package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.course.courseId = :courseId")
    double findAverageRatingByCourseId(@Param("courseId") Integer courseId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.course.teacher.userId = :teacherId")
    double findAverageRatingByTeacherId(@Param("teacherId") Integer teacherId);
}
