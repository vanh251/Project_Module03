package ra.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaginationMeta {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
