package sh.admin.backend.league.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistLeagueResponseDto {
    private Long leagueId;
    private String leagueName;
}
