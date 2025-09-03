package sh.admin.backend.admin.request;

import lombok.Getter;
import sh.admin.backend.common.base.AbstractVO;

@Getter
public class AdminLoginRequestDto extends AbstractVO {
    private String loginId;
    private String password;
}
