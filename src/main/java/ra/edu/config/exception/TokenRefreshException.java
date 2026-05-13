package ra.edu.config.exception;

public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(String token, String message) {
        super(String.format("Lỗi truy cập cho token [%s]: %s", token, message));
    }
}
