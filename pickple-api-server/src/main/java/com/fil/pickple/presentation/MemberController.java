package com.fil.pickple.presentation;

import com.fil.pickple.presentation.response.SearchBaseProfileResponse;
import com.fil.pickple.service.MemberService;
import com.fil.pickple.service.command.SearchBaseProfileCommand;
import com.fil.pickple.service.result.SearchBaseProfileResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{member-id}/profile")
    public ResponseEntity<SearchBaseProfileResponse> searchBaseProfile(@PathVariable("member-id") Long memberId) {
        SearchBaseProfileCommand command = SearchBaseProfileCommand.of(memberId);

        SearchBaseProfileResult result = memberService.searchBaseProfile(command);

        return ResponseEntity.ok(SearchBaseProfileResponse.from(result));
    }
}
