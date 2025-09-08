package sh.admin.backend.member.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberListInfoResponseDto {
    private Long memberId; // 계정 ID
    private String email; // 이메일
    private String nickName; // 닉네임
    private String name; // 이름
    private String accountType; // 계정 유형
    private String live; // 계정 유지 여부
    private String profileImage; // 계정 프로필 이미지 호출 경로
    private String gender; // 성별
    private Long leagueId; // 선호 리그 ID
    private String leagueName; // 선호 리그 명
    private Long teamId; // 선호 팀 ID
    private String teamName; // 선호 팀 명
    private String createdAt; // 생성일자
}
