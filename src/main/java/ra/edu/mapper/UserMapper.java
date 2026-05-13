package ra.edu.mapper;

import org.mapstruct.Mapper;
import ra.edu.dto.request.RegisterRequest;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity (RegisterRequest request);
    UserProfileResponse toUserProfileResponse(User user);
}
