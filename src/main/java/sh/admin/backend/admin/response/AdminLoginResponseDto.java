package sh.admin.backend.admin.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AdminLoginResponseDto {
    private String loginId;
    private String password;
    private String accessToken;
    private String refreshToken;
}
