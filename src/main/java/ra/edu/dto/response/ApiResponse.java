package ra.edu.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Map<String, String> errors;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .errors(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, Map<String, String> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Phương thức tiện ích để tạo phản hồi phân trang

    public static <T> ApiResponse<PageResponse<T>> paginated(String message,
                                               List<T> data,
                                               int page,
                                               int size,
                                               long totalElements
    ) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        PaginationMeta paginationMeta = PaginationMeta.builder()
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();

        PageResponse<T> pageResponse = PageResponse.<T>builder()
                .items(data)
                .pagination(paginationMeta)
                .build();

        return ApiResponse.<PageResponse<T>>builder()
                .success(true)
                .message(message)
                .data(pageResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
