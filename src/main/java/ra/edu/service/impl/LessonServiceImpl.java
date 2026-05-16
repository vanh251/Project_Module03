package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.config.exception.ForbiddenOperationException;
import ra.edu.config.exception.ResourceNotFoundException;
import ra.edu.dto.request.LessonPublishRequest;
import ra.edu.dto.request.LessonRequest;
import ra.edu.dto.response.LessonResponse;
import ra.edu.entity.Lesson;
import ra.edu.entity.Role;
import ra.edu.entity.User;
import ra.edu.mapper.CourseMapper;
import ra.edu.repository.LessonProgressRepository;
import ra.edu.repository.LessonRepository;
import ra.edu.service.LessonService;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
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

    @Override
    @Transactional
    public LessonResponse updateLesson(Integer lessonId, LessonRequest request, User currentUser) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài học"));

        if (currentUser.getRole() == Role.TEACHER) {
            if (!lesson.getCourse().getTeacher().getUserId().equals(currentUser.getUserId())) {
                throw new ForbiddenOperationException("Bạn không có quyền cập nhật bài học trong khóa học này");
            }
        }

        lesson.setTitle(request.getTitle());
        lesson.setContentUrl(request.getContentUrl());
        lesson.setTextContent(request.getTextContent());
        lesson.setOrderIndex(request.getOrderIndex());
        if (request.getIsPublished() != null) {
            lesson.setIsPublished(request.getIsPublished());
        }

        Lesson savedLesson = lessonRepository.save(lesson);
        return courseMapper.toLessonResponse(savedLesson);
    }

    @Override
    @Transactional
    public LessonResponse updateLessonStatus(Integer lessonId, LessonPublishRequest request, User currentUser) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài học"));

        if (currentUser.getRole() == Role.TEACHER) {
            if (!lesson.getCourse().getTeacher().getUserId().equals(currentUser.getUserId())) {
                throw new ForbiddenOperationException("Bạn không có quyền cập nhật bài học trong khóa học này");
            }
        }

        lesson.setIsPublished(request.getIsPublished());

        Lesson savedLesson = lessonRepository.save(lesson);
        return courseMapper.toLessonResponse(savedLesson);
    }

    @Override
    @Transactional
    public void deleteLesson(Integer lessonId, User currentUser) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài học"));

        if (currentUser.getRole() == Role.TEACHER) {
            if (!lesson.getCourse().getTeacher().getUserId().equals(currentUser.getUserId())) {
                throw new ForbiddenOperationException("Bạn không có quyền xóa bài học trong khóa học này");
            }
        }

        if (lessonProgressRepository.existsByLesson(lesson)) {
            throw new ForbiddenOperationException("Không thể xóa bài học vì đã có học viên tham gia học");
        }

        lessonRepository.delete(lesson);
    }
}
