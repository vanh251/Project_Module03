package ra.edu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.LessonResponse;
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
}
