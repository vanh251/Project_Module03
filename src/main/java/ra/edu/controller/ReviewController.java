package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.ReviewRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.ReviewResponse;
import ra.edu.entity.User;
import ra.edu.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;

    @PutMapping("/{review_id}")
    public ApiResponse<ReviewResponse> updateReview(
            @PathVariable("review_id") Integer reviewId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        ReviewResponse response = reviewService.updateReview(reviewId, request, currentUser);
        return ApiResponse.success("Cập nhật đánh giá thành công", response);
    }

    @DeleteMapping("/{review_id}")
    public ApiResponse<Void> deleteReview(
            @PathVariable("review_id") Integer reviewId,
            @AuthenticationPrincipal User currentUser
    ) {
        reviewService.deleteReview(reviewId, currentUser);
        return ApiResponse.success("Xóa đánh giá thành công", null);
    }
}
