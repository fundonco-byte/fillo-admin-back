package sh.admin.backend.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh.admin.backend.team.domain.Team;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // 관리자가 회원 정보 수정 시 수정 선택한 리그에 해당되는 팀 정보들 호출
    List<Team> findAllByLeagueId(Long leagueId);

    // 회원가입 시 유저가 선택한 팀 정보 호출
    Team getTeamByTeamId(Long teamId);
}
