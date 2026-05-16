package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.LessonPublishRequest;
import ra.edu.dto.request.LessonRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.LessonResponse;
import ra.edu.entity.User;
import ra.edu.service.LessonService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lessons")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/{lesson_id}")
    public ApiResponse<LessonResponse> getLessonDetail(@PathVariable("lesson_id") Integer lessonId) {
        LessonResponse response = lessonService.getLessonDetail(lessonId);
        return ApiResponse.success("Chi tiết bài học", response);
    }

    @PutMapping("/{lesson_id}")
    public ApiResponse<LessonResponse> updateLesson(
            @PathVariable("lesson_id") Integer lessonId,
            @Valid @RequestBody LessonRequest request,
            @AuthenticationPrincipal User currentUser) {
        LessonResponse response = lessonService.updateLesson(lessonId, request, currentUser);
        return ApiResponse.success("Cập nhật bài học thành công", response);
    }

    @PutMapping("/{lesson_id}/publish")
    public ApiResponse<LessonResponse> updateLessonStatus(
            @PathVariable("lesson_id") Integer lessonId,
            @Valid @RequestBody LessonPublishRequest request,
            @AuthenticationPrincipal User currentUser) {
        LessonResponse response = lessonService.updateLessonStatus(lessonId, request, currentUser);
        return ApiResponse.success("Cập nhật trạng thái hiển thị thành công", response);
    }

    @DeleteMapping("/{lesson_id}")
    public ApiResponse<Void> deleteLesson(
            @PathVariable("lesson_id") Integer lessonId,
            @AuthenticationPrincipal User currentUser) {
        lessonService.deleteLesson(lessonId, currentUser);
        return ApiResponse.success("Xóa bài học thành công", null);
    }
}
