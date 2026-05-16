package ra.edu.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import ra.edu.entity.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotNull(message = "Quyền hạn không được để trống")
    private Role role; // ADMIN có quyền chọn STUDENT, TEACHER, hoặc ADMIN
}