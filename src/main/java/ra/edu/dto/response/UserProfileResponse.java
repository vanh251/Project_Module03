package ra.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse {
    private Integer userId;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private Boolean isActive;
}
