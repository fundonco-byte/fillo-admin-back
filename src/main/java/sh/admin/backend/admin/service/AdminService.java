package sh.admin.backend.admin.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sh.admin.backend.admin.request.AdminLoginRequestDto;
import sh.admin.backend.admin.response.AdminLoginResponseDto;
import sh.admin.backend.common.base.AbstractExceptionHandler;
import sh.admin.backend.common.util.LogUtil;
import sh.admin.backend.jwt.response.JwtTokenDto;
import sh.admin.backend.jwt.service.JwtTokenProvider;
import sh.admin.backend.share.ResponseBody;
import sh.admin.backend.share.StatusCode;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService extends AbstractExceptionHandler {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${active.host}")
    private String activeHost;

    @Value("${admin.account}")
    private String adminLoginId;

    @Value("${admin.password}")
    private String adminPassword;


    // 관리자 로그인 service
    public ResponseBody loginAccount(HttpServletResponse response, AdminLoginRequestDto adminLoginRequestDto) {
        log.info("관리자 계정 로그인 service");

        try {
            // 로그인 시도 시 해당 계정이 존재하는지 확인
            if (!adminLoginRequestDto.getLoginId().equals(adminLoginId) && !adminLoginRequestDto.getPassword().equals(adminPassword)) {
                log.info("관리자 계정으로 로그인할 수 없습니다.");
                LogUtil.logError("관리자 계정으로 로그인할 수 없습니다.", adminLoginRequestDto);
                return new ResponseBody(StatusCode.NOT_EXIST_ADMIN_ACCOUNT, null);
            }

            // 토큰을 발급하고 Dto 개체에 저장하는 과정
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(adminLoginRequestDto.getLoginId(), adminLoginRequestDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 추출한 JWT를 반환객체에 담기
            JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication, "admin");

            // Response Header에 액세스 토큰 리프레시 토큰, 토큰 만료일 input
            response.addHeader("Authorization", "Bearer " + jwtTokenDto.getAccessToken());
            response.addHeader("RefreshToken", jwtTokenDto.getRefreshToken());
            response.addHeader("AccessTokenExpireTime", jwtTokenDto.getAccessTokenExpiresIn().toString());
            response.addHeader("RefreshTokenExpireTime", jwtTokenDto.getRefreshTokenExpiresIn().toString());

            return new ResponseBody(
                    StatusCode.OK,
                    activeHost.equals("LOCAL") ?
                            AdminLoginResponseDto.builder()
                                    .loginId(adminLoginRequestDto.getLoginId())
                                    .password(adminLoginRequestDto.getPassword())
                                    .accessToken("Bearer " + jwtTokenDto.getAccessToken())
                                    .refreshToken(jwtTokenDto.getRefreshToken())
                                    .build() :
                            "로그인 성공"
            );
        } catch (Exception e) {
            LogUtil.logException(e, response, adminLoginRequestDto);
            return null;
        }
    }


    // 관리자 계정 로그아웃 service
    @Transactional
    public ResponseBody logoutAccount(HttpServletRequest request, HttpServletResponse response) {
        log.info("관리자 계정 로그아웃 service");

        try {
            // Servlet 정보 삭제 및 Session에 넣어진 토큰 정보 삭제
            // 로그아웃 시 헤더에 넣어진 값들은 지울 수 없으므로
            // 이 API가 정상적으로 실행된 이후 앞단에서 헤더에 토큰 주입을 중단 처리 할 것
            request.getSession().invalidate();
            request.logout();
            SecurityContextHolder.clearContext();

            return new ResponseBody(StatusCode.OK, "정상적으로 로그아웃 되셨습니다.");
        } catch (Exception e) {
            LogUtil.logException(e, request, response);
            return null;
        }
    }

}


