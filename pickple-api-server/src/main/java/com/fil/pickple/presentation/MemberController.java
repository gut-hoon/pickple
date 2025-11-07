package com.fil.pickple.presentation;

import com.fil.pickple.application.MemberFacade;
import com.fil.pickple.application.command.RegisterMemberCommand;
import com.fil.pickple.application.result.RegisterMemberResult;
import com.fil.pickple.application.result.SearchMeResult;
import com.fil.pickple.presentation.request.EditMemberRequest;
import com.fil.pickple.presentation.request.RegisterMemberRequest;
import com.fil.pickple.presentation.response.*;
import com.fil.pickple.application.MemberService;
import com.fil.pickple.application.command.EditMemberCommand;
import com.fil.pickple.application.command.SearchBaseProfileCommand;
import com.fil.pickple.application.result.SearchBaseProfileResult;
import com.fil.pickple.presentation.response.SearchMeResponse;
import com.fil.pickple.presentation.response.WithdrawResponse;
import com.fil.pickple.security.provider.JwtTokenProvider;
import com.fil.pickple.security.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberFacade memberFacade;
    private final JwtTokenProvider jwtTokenProvider;

    private final long ACCESS_TOKEN_EXPIRATION;
    private final long REFRESH_TOKEN_EXPIRATION;
    private final String ACCESS_TOKEN_NAME;
    private final String REFRESH_TOKEN_NAME;

    protected MemberController(
            MemberService memberService,
            MemberFacade memberFacade,
            JwtTokenProvider jwtTokenProvider,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            @Value("${jwt.cookie.access-token-name}") String accessTokenName,
            @Value("${jwt.cookie.refresh-token-name}") String refreshTokenName
    ) {
        this.memberService = memberService;
        this.memberFacade = memberFacade;
        this.jwtTokenProvider = jwtTokenProvider;
        this.ACCESS_TOKEN_EXPIRATION = accessTokenExpiration;
        this.REFRESH_TOKEN_EXPIRATION = refreshTokenExpiration;
        this.ACCESS_TOKEN_NAME = accessTokenName;
        this.REFRESH_TOKEN_NAME = refreshTokenName;
    }

    @GetMapping("/{member-id}/profile")
    public ResponseEntity<SearchBaseProfileResponse> searchBaseProfile(@PathVariable("member-id") Long memberId) {
        SearchBaseProfileCommand command = SearchBaseProfileCommand.of(memberId);

        SearchBaseProfileResult result = memberService.searchBaseProfile(command);

        return ResponseEntity.ok(SearchBaseProfileResponse.from(result));
    }

    @GetMapping("/me")
    public ResponseEntity<SearchMeResponse> searchMe() {
        SearchMeResult result = memberService.searchMe();

        return ResponseEntity.ok(SearchMeResponse.from(result));
    }
    @PatchMapping("/me")
    public ResponseEntity<EditMemberResponse> editMember(@Valid @ModelAttribute EditMemberRequest request) {
        EditMemberCommand command = request.toCommand();

        memberService.editMember(command);

        return ResponseEntity.ok(new EditMemberResponse(true));
    }

    @DeleteMapping("/me")
    public ResponseEntity<WithdrawResponse> withdraw(HttpServletRequest request, HttpServletResponse response) {

        String accessToken = CookieUtil.getCookieValue(request, ACCESS_TOKEN_NAME).orElse(null);
        String refreshToken = CookieUtil.getCookieValue(request, REFRESH_TOKEN_NAME).orElse(null);

        memberFacade.withdrawMember(accessToken, refreshToken);

        Cookie accessTokenCookie = CookieUtil.createCookie(ACCESS_TOKEN_NAME, "", 0);
        Cookie refreshTokenCookie = CookieUtil.createCookie(REFRESH_TOKEN_NAME, "", 0);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new WithdrawResponse(true));
    }

    @PatchMapping("/register")
    public ResponseEntity<?> registerMember(@Valid @RequestBody RegisterMemberRequest request, HttpServletResponse response) {
        RegisterMemberCommand command = request.toCommand();
        RegisterMemberResult result = memberService.registerMember(command);

        String newAccessToken = jwtTokenProvider.createAccessToken(
                result.memberId(),
                result.email(),
                true
        );
        String newRefreshToken = jwtTokenProvider.createRefreshToken(result.memberId());

        Cookie accessTokenCookie = CookieUtil.createCookie(
                ACCESS_TOKEN_NAME,
                newAccessToken,
                (int) (ACCESS_TOKEN_EXPIRATION / 1000)
        );

        Cookie refreshTokenCookie = CookieUtil.createCookie(
                REFRESH_TOKEN_NAME,
                newRefreshToken,
                (int) (REFRESH_TOKEN_EXPIRATION / 1000)
        );

        CookieUtil.addCookies(response, accessTokenCookie, refreshTokenCookie);
        return ResponseEntity.ok(new RegisterMemberResponse(true));
    }
}