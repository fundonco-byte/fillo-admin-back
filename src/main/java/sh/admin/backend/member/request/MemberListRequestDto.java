package sh.admin.backend.member.request;

import lombok.Getter;
import sh.admin.backend.common.base.AbstractVO;

@Getter
public class MemberListRequestDto extends AbstractVO {
    private String searchKeyword;
    private String accountType;
    private String gender;
    private Long leagueId;
    private String sort;
    private int page;
}