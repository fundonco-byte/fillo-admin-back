package sh.admin.backend.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Collate;
import org.hibernate.annotations.Comment;
import sh.admin.backend.member.request.MemberUpdateRequestDto;
import sh.admin.backend.share.TimeStamped;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@org.springframework.data.relational.core.mapping.Table
@Comment("회원")
@Entity
@Table(
        indexes = {
                @Index(name = "idx_member_email", columnList = "email"),
//                @Index(name = "idx_member_address", columnList = "address"),
                @Index(name = "idx_member_account_type", columnList = "accountType"),
        }
)
public class Member extends TimeStamped {

    @Comment("회원 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long memberId;

    @Comment("회원 계정 이메일")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(50) not null")
    private String email;

    @Comment("회원 계정 닉네임")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(20)")
    private String nickName;

    @Comment("회원 이름")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(10) not null")
    private String name;

    @Comment("회원 계정 비밀번호")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(70) not null")
    private String password;

    @Comment("회원 계정 유형")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(10) not null")
    private String accountType;

    @Comment("회원 주소")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(150) default null")
    private String address;

    @Comment("회원 생일번호 (예 : 19990101)")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(8) not null")
    private String birthDate;

    @Comment("우편번호")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(6) default null")
    private String postalCode;

    @Comment("전화번호 (예 : 01033334444)")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(15) default null")
    private String phone;

    @Comment("계정 유지 여부")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(1) default 'y'")
    private String live;

    @Comment("계정 프로필 이미지 호출 경로")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(100) default null")
    private String profileImage;

//    @Comment("간단 계정 자기소개")
//    @Collate("utf8mb4_general_ci")
//    @Column(columnDefinition = "text")
//    private String introduceSelf;

    @Comment("성별 (M:남자, F:여자)")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(1) not null")
    private String gender;

    @Comment("선호 리그 ID")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "bigint not null")
    private Long leagueId;

    @Comment("선호 리그 명")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(15) not null")
    private String leagueName;

    @Comment("선호 팀 ID")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "bigint not null")
    private Long teamId;

    @Comment("선호 팀 명")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "varchar(20) not null")
    private String teamName;

    @Comment("개인정보 수집 동의")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(1) not null")
    private String personalInfoAgreement;

    @Comment("마케팅 동의")
    @Collate("utf8mb4_general_ci")
    @Column(columnDefinition = "char(1) not null")
    private String marketingAgreement;

    // 회원 정보 수정
    public void changeMemberInfo(MemberUpdateRequestDto updateInfo) {

        if((this.nickName != null && !this.nickName.equals(updateInfo.getNickName())) || (this.nickName == null && updateInfo.getNickName() != null)){
            this.nickName = updateInfo.getNickName();
        }

        if((this.address != null && !this.address.equals(updateInfo.getAddress())) || (this.address == null && updateInfo.getAddress() != null)){
            this.address = updateInfo.getAddress();
        }

        if((this.birthDate != null && !this.birthDate.equals(updateInfo.getBirthDate())) || (this.birthDate == null && updateInfo.getBirthDate() != null)){
            this.birthDate = updateInfo.getBirthDate();
        }

        if((this.postalCode != null && !this.postalCode.equals(updateInfo.getPostalCode())) || (this.postalCode == null && updateInfo.getPostalCode() != null)){
            this.postalCode = updateInfo.getPostalCode();
        }

        if((this.phone != null && !this.phone.equals(updateInfo.getPhone())) || (this.phone == null && updateInfo.getPhone() != null)){
            this.phone = updateInfo.getPhone();
        }

        if((this.live != null && !this.live.equals(updateInfo.getLive())) || (this.live == null && updateInfo.getLive() != null)){
            this.live = updateInfo.getLive();
        }

        if((this.gender != null && !this.gender.equals(updateInfo.getGender())) || (this.gender == null && updateInfo.getGender() != null)){
            this.gender = updateInfo.getGender();
        }

        if((this.leagueId != null && !this.leagueId.equals(updateInfo.getLeagueId())) || (this.leagueId == null && updateInfo.getLeagueId() != null)){
            this.leagueId = updateInfo.getLeagueId();
        }

        if((this.leagueName != null && !this.leagueName.equals(updateInfo.getLeagueName())) || (this.leagueName == null && updateInfo.getLeagueName() != null)){
            this.leagueName = updateInfo.getLeagueName();
        }

        if((this.teamId != null && !this.teamId.equals(updateInfo.getTeamId())) || (this.teamId == null && updateInfo.getTeamId() != null)){
            this.teamId = updateInfo.getTeamId();
        }

        if((this.teamName != null && !this.teamName.equals(updateInfo.getTeamName())) || (this.teamName == null && updateInfo.getTeamName() != null)){
            this.teamName = updateInfo.getTeamName();
        }

        if((this.personalInfoAgreement != null && !this.personalInfoAgreement.equals(updateInfo.getPersonalInfoAgreement())) || (this.personalInfoAgreement == null && updateInfo.getPersonalInfoAgreement() != null)){
            this.personalInfoAgreement = updateInfo.getPersonalInfoAgreement();
        }

        if((this.marketingAgreement != null && !this.marketingAgreement.equals(updateInfo.getMarketingAgreement())) || (this.marketingAgreement == null && updateInfo.getMarketingAgreement() != null)){
            this.marketingAgreement = updateInfo.getMarketingAgreement();
        }
    }
}
