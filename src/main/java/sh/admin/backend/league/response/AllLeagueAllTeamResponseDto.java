package sh.admin.backend.league.response;

import lombok.Builder;
import lombok.Getter;
import sh.admin.backend.team.response.RegistTeamResponseDto;

import java.util.List;

@Getter
@Builder
public class AllLeagueAllTeamResponseDto {
    private Long leagueId;
    private String leagueName;
    private List<RegistTeamResponseDto> teamList;
}
