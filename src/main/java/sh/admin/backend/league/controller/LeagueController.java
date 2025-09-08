package sh.admin.backend.league.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sh.admin.backend.aop.MethodCallMonitor;
import sh.admin.backend.aop.TimeMonitor;
import sh.admin.backend.common.util.LogUtil;
import sh.admin.backend.league.service.LeagueService;
import sh.admin.backend.share.ResponseBody;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/league")
@RestController
public class LeagueController {

    private final LeagueService leagueService;

    // 관리자 회원 리스트 검색 시 노출할 리그 필터링 리스트 데이터
    @MethodCallMonitor
    @TimeMonitor
    @GetMapping(value = "/all", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> getAllLeague(
            HttpServletRequest request){
        log.info("[League] 관리자 회원 리스트 검색 시 노출할 리그 필터링 리스트 데이터");

        try{
            return new ResponseEntity<>(leagueService.getAllLeague(request), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request);
            return null;
        }
    }


    // 관리자가 특정 회원 정보 수정 시 가지고 있는 리그 정보와 연관된 팀 정보들 전부 호출
    @MethodCallMonitor
    @TimeMonitor
    @GetMapping(value = "/all/relate", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> getAllLeagueAllTeam(
            HttpServletRequest request){
        log.info("[League] 관리자가 특정 회원 정보 수정 시 가지고 있는 리그 정보와 연관된 팀 정보들 전부 호출");

        try{
            return new ResponseEntity<>(leagueService.getAllLeagueAllTeam(request), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request);
            return null;
        }
    }
}
