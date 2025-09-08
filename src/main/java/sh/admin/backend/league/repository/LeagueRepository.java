package sh.admin.backend.league.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.admin.backend.league.domain.League;

import java.util.List;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {

    // 관리자 회원 리스트 검색 시 노출할 리그 필터링 리스트 데이터 호출
    List<League> findAllBy();

    // 회원가입 시 유저가 선택한 리그 정보 호출
    League getLeagueByLeagueId(Long leagueId);
}
