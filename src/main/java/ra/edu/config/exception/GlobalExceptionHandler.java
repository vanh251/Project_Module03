package ra.edu.config.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ra.edu.dto.response.ApiResponse;

import java.security.SignatureException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        log.warn("Dữ liệu đầu vào không hợp lệ: {}", errors);
        return new ResponseEntity<>(ApiResponse.error("Dữ liệu đầu vào không hợp lệ", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex){
        Map<String, String > error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("Tài nguyên không tìm thấy: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("Không tìm thấy tài nguyên", error), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequestException(BadRequestException ex){
        Map<String, String > error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("Lỗi yêu cầu: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("Yêu cầu không hợp lệ", error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("Tài nguyên đã tồn tại: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("Tài nguyên đã tồn tại", error), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        Map<String, String> error = new HashMap<>();
        String message = ex.getMessage();
        if (message.contains("username")) {
            error.put("error", "Tên đăng nhập đã tồn tại");
            log.warn("Lỗi lưu dữ liệu - Username trùng lặp");
            return new ResponseEntity<>(ApiResponse.error("Tên đăng nhập đã tồn tại", error), HttpStatus.CONFLICT);
        } else if (message.contains("email")) {
            error.put("error", "Email đã được đăng ký");
            log.warn("Lỗi lưu dữ liệu - Email trùng lặp");
            return new ResponseEntity<>(ApiResponse.error("Email đã được đăng ký", error), HttpStatus.CONFLICT);
        }
        error.put("error", "Dữ liệu không hợp lệ hoặc đã tồn tại");
        log.error("Lỗi ràng buộc dữ liệu: {}", message, ex);
        return new ResponseEntity<>(ApiResponse.error("Dữ liệu không hợp lệ hoặc đã tồn tại", error), HttpStatus.CONFLICT);
    }


    //Jwt exception
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<?>> handleExpiredJwtException(ExpiredJwtException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("Token đã hết hạn: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("Token đã hết hạn. Vui lòng đăng nhập lại.", error), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<?>> handleMalformedJwtException(MalformedJwtException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("Token bị sai định dạng: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("Token bị sai định dạng. Vui lòng kiểm tra lại.", error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<?>> handleSignatureException(SignatureException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("Token có chữ ký không hợp lệ: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("Token có chữ ký không hợp lệ. Vui lòng kiểm tra lại.", error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ApiResponse<?>> handleUnsupportedJwtException(UnsupportedJwtException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("Hệ thống không hỗ trợ loại token này: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("Hệ thống không hỗ trợ loại token này. Vui lòng kiểm tra lại.", error), HttpStatus.BAD_REQUEST);
    }

    // Spring Security exception
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentialsException(BadCredentialsException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("Lỗi xác thực: Sai username hoặc password: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("Sai username hoặc password!", error), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<?>> handleDisabledException(DisabledException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Tài khoản của bạn đã bị vô hiệu hóa");
        log.warn("Tài khoản bị vô hiệu hóa cố gắng đăng nhập: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên!", error), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ApiResponse<?>> handleForbiddenOperationException(ForbiddenOperationException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("Thao tác bị cấm: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), error), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedSecurityException(AccessDeniedException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Bạn không có quyền truy cập tài nguyên này");
        log.warn("Lỗi quyền truy cập: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("Bạn không có quyền truy cập tài nguyên này!", error), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.error("Lỗi không xác định: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.error("Lỗi máy chủ nội bộ. Vui lòng thử lại sau hoặc liên hệ quản trị viên.", error), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
