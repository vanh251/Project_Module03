package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ra.edu.entity.Role;

@Data
public class UpdateRoleRequest {
    @NotNull(message = "Vui lòng chọn vai trò mới")
    private Role role;
}