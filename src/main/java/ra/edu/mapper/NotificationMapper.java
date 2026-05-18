package ra.edu.mapper;

import org.mapstruct.Mapper;
import ra.edu.dto.response.NotificationResponse;
import ra.edu.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);
}
