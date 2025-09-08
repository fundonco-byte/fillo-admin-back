package sh.admin.backend.league.service;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sh.admin.backend.common.util.LogUtil;
import sh.admin.backend.jwt.service.JwtTokenProvider;
import sh.admin.backend.league.domain.League;
import sh.admin.backend.league.repository.LeagueRepository;
import sh.admin.backend.league.response.AllLeagueAllTeamResponseDto;
import sh.admin.backend.league.response.RegistLeagueResponseDto;
import sh.admin.backend.member.domain.Member;
import sh.admin.backend.share.ResponseBody;
import sh.admin.backend.share.StatusCode;
import sh.admin.backend.team.domain.Team;
import sh.admin.backend.team.repository.TeamRepository;
import sh.admin.backend.team.response.RegistTeamResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TeamRepository teamRepository;

    // 관리자 회원 리스트 검색 시 노출할 리그 필터링 리스트 데이터 service
    public ResponseBody getAllLeague(HttpServletRequest request) {
        log.info("관리자 회원 리스트 검색 시 노출할 리그 필터링 리스트 데이터 service");

        try {
            // 관리자 계정 로그인 토큰 정상 확인
            if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7)) && !jwtTokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                log.info("옳바른 토큰 정보가 아니라 정보를 조회할 수 없습니다.");
                LogUtil.logError("옳바른 토큰 정보가 아니라 정보를 조회할 수 없습니다.", request.getHeader("Authorization"));
                return new ResponseBody(StatusCode.UNSUPPORTED_TOKEN, null);
            }

            // 전체 리그 정보 호출
            List<League> allLeague = leagueRepository.findAllBy();

            // 리그 정보가 하나도 존재하지 않는지 확인
            if (allLeague.isEmpty()) {
                LogUtil.logError(StatusCode.EMPTY_LEAGUE_INFO.getMessage(), allLeague);
                return new ResponseBody(StatusCode.EMPTY_LEAGUE_INFO, null);
            }

            // 호출한 전체 리그 정보들을 반환 객체로 변환
            List<RegistLeagueResponseDto> result = allLeague.stream()
                    .map(eachLeague ->
                            RegistLeagueResponseDto.builder()
                                    .leagueId(eachLeague.getLeagueId())
                                    .leagueName(eachLeague.getName())
                                    .build()
                    ).collect(Collectors.toList());

            return new ResponseBody(StatusCode.OK, result);
        } catch (Exception e) {
            LogUtil.logException(e, request);
            return null;
        }
    }


    // 관리자가 특정 회원 정보 수정 시 가지고 있는 리그 정보와 연관된 팀 정보들 전부 호출 service
    public ResponseBody getAllLeagueAllTeam(HttpServletRequest request) {
        log.info("관리자가 특정 회원 정보 수정 시 가지고 있는 리그 정보와 연관된 팀 정보들 전부 호출 service");

        try {
            // 관리자 계정 로그인 토큰 정상 확인
            if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7)) && !jwtTokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                log.info("옳바른 토큰 정보가 아니라 정보를 수정할 수 없습니다.");
                LogUtil.logError("옳바른 토큰 정보가 아니라 정보를 수정할 수 없습니다.", request.getHeader("Authorization"));
                return new ResponseBody(StatusCode.UNSUPPORTED_TOKEN, null);
            }

            // 전체 리그 정보 호출
            List<League> allLeague = leagueRepository.findAllBy();

            // 리그 정보가 하나도 존재하지 않는지 확인
            if (allLeague.isEmpty()) {
                LogUtil.logError(StatusCode.EMPTY_LEAGUE_INFO.getMessage(), allLeague);
                return new ResponseBody(StatusCode.EMPTY_LEAGUE_INFO, null);
            }

            // 호출한 전체 리그 정보들을 반환 객체로 변환
            List<AllLeagueAllTeamResponseDto> result = allLeague.stream()
                    .map(eachLeague -> {
                                // 전체 리그에 해당하는 각 리그의 팀들 정보 호출
                                List<Team> relatedTeams = teamRepository.findAllByLeagueId(eachLeague.getLeagueId());

                                // 호출한 팀들 정보를 반환 리스트 객체로 매핑
                                List<RegistTeamResponseDto> relatedTeamList = relatedTeams.stream()
                                        .map((eachTeam) ->
                                                RegistTeamResponseDto.builder()
                                                        .teamId(eachTeam.getTeamId())
                                                        .teamName(eachTeam.getName())
                                                        .build()
                                        ).toList();

                                // 각 리그 별 매핑된 팀들 정보를 포함한 최종 반환 객체 반환
                                return AllLeagueAllTeamResponseDto.builder()
                                        .leagueId(eachLeague.getLeagueId())
                                        .leagueName(eachLeague.getName())
                                        .teamList(relatedTeamList)
                                        .build();
                            }
                    ).collect(Collectors.toList());

            return new ResponseBody(StatusCode.OK, result);
        } catch (Exception e) {
            LogUtil.logException(e, request);
            return null;
        }
    }
}
