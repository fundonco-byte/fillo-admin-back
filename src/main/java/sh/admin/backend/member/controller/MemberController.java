package sh.admin.backend.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sh.admin.backend.aop.MethodCallMonitor;
import sh.admin.backend.aop.TimeMonitor;
import sh.admin.backend.common.util.LogUtil;
import sh.admin.backend.member.request.MemberListRequestDto;
import sh.admin.backend.member.request.MemberUpdateRequestDto;
import sh.admin.backend.member.service.MemberService;
import sh.admin.backend.share.ResponseBody;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원 리스트 조회
    @MethodCallMonitor
    @TimeMonitor
    @GetMapping(value = "/list", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> getMemberList(
            HttpServletRequest request,
            @RequestBody MemberListRequestDto memberListRequestDto){
        log.info("[Member] 회원 리스트 조회");

        try{
            return new ResponseEntity<>(memberService.getMemberList(request, memberListRequestDto), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, memberListRequestDto);
            return null;
        }
    }


    // 회원 정보 상세 조회
    @MethodCallMonitor
    @TimeMonitor
    @GetMapping(value = "/info", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> getAccountDetailInfo(
            HttpServletRequest request,
            @RequestParam Long m){
        log.info("[Member] 회원 정보 상세 조회");

        try{
            return new ResponseEntity<>(memberService.getAccountDetailInfo(request, m), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request);
            return null;
        }
    }


    // 특정 회원 정보 수정
    @MethodCallMonitor
    @TimeMonitor
    @PutMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> updateAccountInfo(
            HttpServletRequest request,
            @Valid @RequestBody MemberUpdateRequestDto memberUpdateInfoRequestDto){
        log.info("[Member] 특정 회원 정보 수정");

        try{
            return new ResponseEntity<>(memberService.updateAccountInfo(request, memberUpdateInfoRequestDto), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, memberUpdateInfoRequestDto);
            return null;
        }
    }


    // 특정 회원 삭제
    @MethodCallMonitor
    @TimeMonitor
    @DeleteMapping (value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> deleteAccount(
            HttpServletRequest request,
            @RequestParam Long m){
        log.info("[Member] 특정 회원 삭제");

        try{
            return new ResponseEntity<>(memberService.deleteAccount(request, m), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, m);
            return null;
        }
    }

}
