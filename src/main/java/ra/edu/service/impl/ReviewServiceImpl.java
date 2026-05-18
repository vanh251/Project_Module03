package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.config.exception.ForbiddenOperationException;
import ra.edu.config.exception.ResourceNotFoundException;
import ra.edu.dto.request.ReviewRequest;
import ra.edu.dto.response.ReviewResponse;
import ra.edu.entity.Review;
import ra.edu.entity.Role;
import ra.edu.entity.User;
import ra.edu.repository.ReviewRepository;
import ra.edu.service.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public ReviewResponse updateReview(Integer reviewId, ReviewRequest request, User currentUser) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đánh giá"));

        // Kiểm tra quyền: Chỉ ADMIN hoặc STUDENT là tác giả của đánh giá mới được sửa
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = currentUser.getRole() == Role.STUDENT && review.getStudent().getUserId().equals(currentUser.getUserId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenOperationException("Bạn không có quyền cập nhật đánh giá này");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);

        return ReviewResponse.builder()
                .reviewId(updatedReview.getReviewId())
                .studentName(updatedReview.getStudent().getFullName())
                .rating(updatedReview.getRating())
                .comment(updatedReview.getComment())
                .createdAt(updatedReview.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public void deleteReview(Integer reviewId, User currentUser) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đánh giá"));

        // Kiểm tra quyền: Chỉ ADMIN hoặc STUDENT là tác giả của đánh giá mới được xóa
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = currentUser.getRole() == Role.STUDENT && review.getStudent().getUserId().equals(currentUser.getUserId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenOperationException("Bạn không có quyền xóa đánh giá này");
        }

        reviewRepository.delete(review);
    }
}
