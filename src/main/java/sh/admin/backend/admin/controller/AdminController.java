package sh.admin.backend.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sh.admin.backend.admin.request.AdminLoginRequestDto;
import sh.admin.backend.admin.service.AdminService;
import sh.admin.backend.aop.MethodCallMonitor;
import sh.admin.backend.aop.TimeMonitor;
import sh.admin.backend.common.util.LogUtil;
import sh.admin.backend.share.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    // 로그인
    @MethodCallMonitor
    @TimeMonitor
    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> loginAccount(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AdminLoginRequestDto adminLoginRequestDto){
        log.info("[Admin] 로그인");

        try{
            return new ResponseEntity<>(adminService.loginAccount(response, adminLoginRequestDto), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, adminLoginRequestDto);
            return null;
        }
    }


    // 로그아웃
    @MethodCallMonitor
    @TimeMonitor
    @DeleteMapping (value = "/logout", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<ResponseBody> logoutAccount(
            HttpServletRequest request,
            HttpServletResponse response){
        log.info("[Admin] 로그아웃");

        try{
            return new ResponseEntity<>(adminService.logoutAccount(request, response), HttpStatus.OK);
        }catch(Exception e){
            LogUtil.logException(e, request, response);
            return null;
        }
    }

}
