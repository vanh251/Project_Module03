package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.config.exception.ResourceNotFoundException;
import ra.edu.dto.response.LessonResponse;
import ra.edu.entity.Lesson;
import ra.edu.mapper.CourseMapper;
import ra.edu.repository.LessonRepository;
import ra.edu.service.LessonService;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional(readOnly = true)
    public LessonResponse getLessonDetail(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài học"));

        if (!lesson.getIsPublished()) {
            throw new ResourceNotFoundException("Bài học không tồn tại hoặc chưa được xuất bản");
        }

        return courseMapper.toLessonResponse(lesson);
    }
}
