package ra.edu.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T>{
    private List<T> items;
    private PaginationMeta pagination;
}
