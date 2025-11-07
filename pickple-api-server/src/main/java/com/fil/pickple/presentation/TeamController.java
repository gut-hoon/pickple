package com.fil.pickple.presentation;

import com.fil.pickple.application.command.SearchTeamInviteCodeCommand;
import com.fil.pickple.application.command.SearchTeamProfileCommand;
import com.fil.pickple.application.command.UpdateTeamProfileCommand;
import com.fil.pickple.application.result.FindTeamsPickedMeResult;
import com.fil.pickple.application.result.CreateTeamResult;
import com.fil.pickple.application.result.FindTeamsResult;
import com.fil.pickple.application.result.SearchTeamInviteCodeResult;
import com.fil.pickple.application.result.SearchTeamProfileResult;
import com.fil.pickple.application.result.UpdateTeamProfileResult;
import com.fil.pickple.presentation.request.CreateTeamRequest;
import com.fil.pickple.presentation.request.UpdateTeamProfileRequest;
import com.fil.pickple.application.TeamService;
import com.fil.pickple.application.command.CreateTeamCommand;
import com.fil.pickple.presentation.response.FindTeamsPickedMeResponse;
import com.fil.pickple.presentation.response.CreateTeamResponse;
import com.fil.pickple.presentation.response.FindTeamsResponse;
import com.fil.pickple.presentation.response.SearchTeamInviteCodeResponse;
import com.fil.pickple.presentation.response.SearchTeamProfileResponse;
import com.fil.pickple.presentation.response.UpdateTeamProfileResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<CreateTeamResponse> createTeam(@ModelAttribute @Valid CreateTeamRequest createTeamRequest){
        CreateTeamCommand command = createTeamRequest.toCommand();

        CreateTeamResult result = teamService.createTeam(command);

        return ResponseEntity.ok(CreateTeamResponse.from(result));
    }

    @GetMapping("/{team-id}/profile")
    public ResponseEntity<SearchTeamProfileResponse> searchTeam(@PathVariable("team-id") Long teamId){
        SearchTeamProfileCommand command = SearchTeamProfileCommand.of(teamId);

        SearchTeamProfileResult result = teamService.searchTeamProfile(command);

        return ResponseEntity.ok(SearchTeamProfileResponse.from(result));
    }

    @PatchMapping("/{team-id}/profile")
    public ResponseEntity<UpdateTeamProfileResponse> updateTeam(
            @PathVariable("team-id") Long teamId,
            @ModelAttribute @Valid UpdateTeamProfileRequest request
    ){
        UpdateTeamProfileCommand command = request.toCommand(teamId);

        UpdateTeamProfileResult result = teamService.updateTeamProfile(command);

        return ResponseEntity.ok(UpdateTeamProfileResponse.from(result));
    }

    @GetMapping("/me")
    public ResponseEntity<FindTeamsResponse> findTeams() {
        FindTeamsResult result = teamService.findTeams();

        return ResponseEntity.ok(FindTeamsResponse.from(result));
    }

    @GetMapping("/picked-me")
    public ResponseEntity<FindTeamsPickedMeResponse> findTeamsPickedMe() {
        FindTeamsPickedMeResult result = teamService.findTeamsPickedMe();

        return ResponseEntity.ok(FindTeamsPickedMeResponse.from(result));
    }

    @GetMapping("/{team-id}/invite-code")
    public ResponseEntity<SearchTeamInviteCodeResponse> getTeamInviteCode(@PathVariable("team-id") Long teamId) {
        SearchTeamInviteCodeCommand command = SearchTeamInviteCodeCommand.of(teamId);

        SearchTeamInviteCodeResult result = teamService.searchTeamInviteCode(command);

        return ResponseEntity.ok(SearchTeamInviteCodeResponse.from(result));
    }
}
