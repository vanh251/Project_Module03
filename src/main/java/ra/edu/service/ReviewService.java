package ra.edu.service;

import ra.edu.dto.request.ReviewRequest;
import ra.edu.dto.response.ReviewResponse;
import ra.edu.entity.User;

public interface ReviewService {
    ReviewResponse updateReview(Integer reviewId, ReviewRequest request, User currentUser);
    void deleteReview(Integer reviewId, User currentUser);
}
