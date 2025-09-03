package sh.admin.backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sh.admin.backend.member.domain.Member;

import java.util.List;
import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor {
    // 이메일 정보를 통한 회원 계정 조회
    Optional<Member> getMemberByEmail(String loginEmail);

    // 사전 등록한 회원 리스트 조회
//    @Query("SELECT m FROM Member m " +
//            "WHERE (:searchKeyword)" +
//            "AND (:accountType)" +
//            "AND (:gender)" +
//            "AND (:leagueId)" +
//            "ORDER BY :sort")
//    List<Member> getListMembers(
//            @Param("searchKeyword") String searchKeyword,
//            @Param("accountType") String accountType,
//            @Param("gender") String gender,
//            @Param("leagueId") String leagueId,
//            @Param("sort") String sort);

    // 회원 정보 상세 조회
    Member getMemberByMemberId(Long memberId);

    // 특정 회원 삭제
    void deleteMemberByMemberId(Long memberId);
}
