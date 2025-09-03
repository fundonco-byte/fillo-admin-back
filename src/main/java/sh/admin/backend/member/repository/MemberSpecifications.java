package sh.admin.backend.member.repository;

import org.springframework.data.jpa.domain.Specification;
import sh.admin.backend.member.domain.Member;

public class MemberSpecifications {

    // 이름 동적 조건
    public static Specification<Member> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null || name.isEmpty() ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    // 닉네임 동적 조건
    public static Specification<Member> hasNickName(String nickName) {
        return (root, query, criteriaBuilder) ->
                nickName == null || nickName.isEmpty() ? null : criteriaBuilder.like(root.get("nickName"), "%" + nickName + "%");
    }

    // 이메일 동적 조건
    public static Specification<Member> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null || email.isEmpty() ? null : criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    // 계정 유형 동적 조건
    public static Specification<Member> hasAccountType(String accountType) {
        return (root, query, criteriaBuilder) ->
                accountType == null || accountType.isEmpty() ? null : criteriaBuilder.equal(root.get("accountType"), accountType);
    }

    // 성별 동적 조건
    public static Specification<Member> hasGender(String gender) {
        return (root, query, criteriaBuilder) ->
                gender == null || gender.isEmpty() ? null : criteriaBuilder.equal(root.get("gender"), gender);
    }

    // 리그 ID 동적 조건
    public static Specification<Member> hasLeagueId(Long leagueId) {
        return (root, query, criteriaBuilder) ->
                leagueId == null || leagueId == 0L ? null : criteriaBuilder.equal(root.get("leagueId"), leagueId);
    }
}
