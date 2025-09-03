package sh.admin.backend.member.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.admin.backend.common.base.AbstractExceptionHandler;
import sh.admin.backend.common.util.LogUtil;
import sh.admin.backend.jwt.repository.JwtTokenRepository;
import sh.admin.backend.jwt.service.JwtTokenProvider;
import sh.admin.backend.mail.MailService;
import sh.admin.backend.media.service.MediaUpload;
import sh.admin.backend.member.domain.Member;
import sh.admin.backend.member.repository.MemberRepository;
import sh.admin.backend.member.repository.MemberSpecifications;
import sh.admin.backend.member.request.MemberListRequestDto;
import sh.admin.backend.member.request.MemberUpdateRequestDto;
import sh.admin.backend.member.response.MemberInfoResponseDto;
import sh.admin.backend.member.response.MemberListInfoResponseDto;
import sh.admin.backend.share.ResponseBody;
import sh.admin.backend.share.StatusCode;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService extends AbstractExceptionHandler {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;
    private final MediaUpload mediaUpload;
    private final MailService mailService;

    @Value("${active.host}")
    private String activeHost;


    // 회원 리스트 조회 service
    public ResponseBody getMemberList(HttpServletRequest request, MemberListRequestDto memberListRequestDto) {
        log.info("회원 리스트 조회 service");

        try {
            // 관리자 계정 로그인 토큰 정상 확인
            if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7)) && !jwtTokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                log.info("옳바른 토큰 정보가 아니라 회원 리스트를 조회할 수 없습니다.");
                LogUtil.logError("옳바른 토큰 정보가 아니라 회원 리스트를 조회할 수 없습니다.", !jwtTokenProvider.validateToken(request.getHeader("Authorization")) ? request.getHeader("Authorization") : request.getHeader("RefreshToken"));
                return new ResponseBody(StatusCode.UNSUPPORTED_TOKEN, null);
            }

            // 필터링 정보를 통한 동적 쿼리 회원 Spec 설정
            Specification<Member> spec = (MemberSpecifications.hasName(memberListRequestDto.getSearchKeyword())
                    .or(MemberSpecifications.hasNickName(memberListRequestDto.getSearchKeyword()))
                    .or(MemberSpecifications.hasEmail(memberListRequestDto.getSearchKeyword())))
                    .and(MemberSpecifications.hasAccountType(memberListRequestDto.getAccountType()))
                    .and(MemberSpecifications.hasGender(memberListRequestDto.getGender()))
                    .and(MemberSpecifications.hasLeagueId(memberListRequestDto.getLeagueId()));

            // 기본 정렬 (최신순)
            Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

            // 회원 리스트 정렬 값
            switch (memberListRequestDto.getSort()) {
                case "latest": // 최신순
                    sort = Sort.by(Sort.Direction.DESC, "createdAt");
                case "oldest": // 오랜된순
                    sort = Sort.by(Sort.Direction.ASC, "createdAt");
                case "email": // 이메일순
                    sort = Sort.by(Sort.Direction.ASC, "email");
            };

            // 페이징, 정렬 처리 설정 Pageable 객체 생성
            Pageable pageable = PageRequest.of(memberListRequestDto.getPage() - 1, 15, sort);

            // Spec과 Pageable 객체를 통한 페이징 처리된 Member 객체들 호출
            Page<Member> memberList = memberRepository.findAll(spec, pageable);
            
            // 호출 리스트 데이터를 반환 객체로 매핑
            List<MemberListInfoResponseDto> result = memberList.stream()
                    .map(eachMember ->
                            MemberListInfoResponseDto.builder()
                                    .memberId(eachMember.getMemberId())
                                    .email(eachMember.getEmail())
                                    .nickName(eachMember.getNickName())
                                    .name(eachMember.getName())
                                    .accountType(eachMember.getAccountType().equals("default") ? "일반" : "카카오")
                                    .live(eachMember.getLive().equals("y") ? "활성" : "비활성")
                                    .profileImage(eachMember.getProfileImage())
                                    .gender(eachMember.getGender().equals("M") ? "남성" : "여성")
                                    .leagueId(eachMember.getLeagueId())
                                    .leagueName(eachMember.getLeagueName())
                                    .teamId(eachMember.getTeamId())
                                    .teamName(eachMember.getTeamName())
                                    .build()
                    ).collect(Collectors.toList());

            return new ResponseBody(StatusCode.OK, result);
        } catch (Exception e) {
            LogUtil.logException(e, request, memberListRequestDto);
            return null;
        }
    }


    // 회원 정보 상세 조회 service
    public ResponseBody getAccountDetailInfo(HttpServletRequest request, Long memberId) {
        log.info("회원 정보 상세 조회 service");

        try {
            // 관리자 계정 로그인 토큰 정상 확인
            if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7)) && !jwtTokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                log.info("옳바른 토큰 정보가 아니라 정보를 조회할 수 없습니다.");
                LogUtil.logError("옳바른 토큰 정보가 아니라 정보를 조회할 수 없습니다.", request.getHeader("Authorization"));
                return new ResponseBody(StatusCode.CANT_GET_MEMBER_INFO, null);
            }

            // 정보 조회할 회원 객체 호출
            Member getMember = memberRepository.getMemberByMemberId(memberId);

            return new ResponseBody(
                    StatusCode.OK,
                    MemberInfoResponseDto.builder()
                            .memberId(getMember.getMemberId())
                            .email(getMember.getEmail())
                            .nickName(getMember.getNickName() == null ? getMember.getName() : getMember.getNickName())
                            .name(getMember.getName())
                            .accountType(getMember.getAccountType().equals("default") ? "일반" : "카카오")
                            .address(getMember.getAddress())
                            .birthDate(getMember.getBirthDate())
                            .postalCode(getMember.getPostalCode())
                            .phone(getMember.getPhone())
                            .live(getMember.getLive().equals("y") ? "활성" : "비활성")
                            .profileImage(getMember.getProfileImage())
                            .gender(getMember.getGender().equals("M") ? "남성" : "여성")
                            .leagueId(getMember.getLeagueId())
                            .leagueName(getMember.getLeagueName())
                            .teamId(getMember.getTeamId())
                            .teamName(getMember.getTeamName())
                            .personalInfoAgreement(getMember.getPersonalInfoAgreement())
                            .marketingAgreement(getMember.getMarketingAgreement())
                            .build());
        } catch (Exception e) {
            LogUtil.logException(e, request);
            return null;
        }
    }


    // 특정 회원 정보 수정 service
    @Transactional
    public ResponseBody updateAccountInfo(HttpServletRequest request, MemberUpdateRequestDto memberUpdateInfoRequestDto) {
        log.info("특정 회원 정보 수정 service");

        try {
            // 관리자 계정 로그인 토큰 정상 확인
            if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7)) && !jwtTokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                log.info("옳바른 토큰 정보가 아니라 정보를 수정할 수 없습니다.");
                LogUtil.logError("옳바른 토큰 정보가 아니라 정보를 수정할 수 없습니다.", request.getHeader("Authorization"));
                return new ResponseBody(StatusCode.CANT_GET_MEMBER_INFO, null);
            }

            // 수정할 회원 정보 호출
            Member updateMember = memberRepository.getMemberByMemberId(memberUpdateInfoRequestDto.getMemberId());

            // 회원 정보 수정
            updateMember.changeMemberInfo(memberUpdateInfoRequestDto);

            return new ResponseBody(StatusCode.OK, "정상적으로 수정되었습니다.");
        } catch (Exception e) {
            LogUtil.logException(e, request);
            return null;
        }
    }


    // 특정 회원 삭제 service
    @Transactional
    public ResponseBody deleteAccount(HttpServletRequest request, Long memberId) {
        log.info("특정 회원 삭제 service");

        try {
            // 관리자 계정 로그인 토큰 정상 확인
            if (!jwtTokenProvider.validateToken(request.getHeader("Authorization").substring(7)) && !jwtTokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                log.info("옳바른 토큰 정보가 아니라 정보를 수정할 수 없습니다.");
                LogUtil.logError("옳바른 토큰 정보가 아니라 정보를 수정할 수 없습니다.", request.getHeader("Authorization"));
                return new ResponseBody(StatusCode.CANT_GET_MEMBER_INFO, null);
            }

            // 특정 회원 삭제
            memberRepository.deleteMemberByMemberId(memberId);

            // 특정 회원 토큰 삭제
            jwtTokenRepository.deleteJwtTokenByMemberId(memberId);

            return new ResponseBody(StatusCode.OK, "회원 삭제 완료");
        } catch (Exception e) {
            LogUtil.logException(e, request, memberId);
            return null;
        }
    }

}


