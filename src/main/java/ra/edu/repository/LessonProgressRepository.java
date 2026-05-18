package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Enrollment;
import ra.edu.entity.Lesson;
import ra.edu.entity.LessonProgress;

import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Integer> {
    boolean existsByLesson(Lesson lesson);
    Optional<LessonProgress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);
    long countByEnrollmentAndIsCompletedTrue(Enrollment enrollment);
}
