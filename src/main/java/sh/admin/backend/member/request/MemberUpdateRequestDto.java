package sh.admin.backend.member.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sh.admin.backend.common.base.AbstractVO;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberUpdateRequestDto extends AbstractVO {
    private Long memberId; // 계정 ID
    private String nickName; // 닉네임
    private String address; // 주소
    private String birthDate; // 생년월일
    private String postalCode; // 우편번호
    private String phone; // 전화번호
    private String live; // 계정 유지 여부
    private String profileImage; // 계정 프로필 이미지 호출 경로
    private String gender; // 성별
    private Long leagueId; // 선호 리그 ID
    private String leagueName; // 선호 리그 명
    private Long teamId; // 선호 팀 ID
    private String teamName; // 선호 팀 명
    private String personalInfoAgreement; // 개인정보 이용 동의
    private String marketingAgreement; // 마케팅 동의
}
