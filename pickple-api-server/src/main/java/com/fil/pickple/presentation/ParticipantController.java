package com.fil.pickple.presentation;

import com.fil.pickple.application.ParticipantService;
import com.fil.pickple.application.command.LeaveTeamCommand;
import com.fil.pickple.application.command.JoinTeamCommand;
import com.fil.pickple.application.command.SearchParticipantDetailCommand;
import com.fil.pickple.application.command.SearchParticipantProfileCommand;
import com.fil.pickple.application.command.EditParticipantCommand;
import com.fil.pickple.application.result.SearchParticipantDetailResult;
import com.fil.pickple.application.result.SearchParticipantProfileResult;
import com.fil.pickple.application.result.EditParticipantResult;
import com.fil.pickple.application.result.SearchTeamParticipantsResult;
import com.fil.pickple.presentation.request.JoinTeamRequest;
import com.fil.pickple.presentation.request.EditParticipantRequest;
import com.fil.pickple.presentation.response.DeleteParticipantResponse;
import com.fil.pickple.presentation.response.JoinTeamResponse;
import com.fil.pickple.presentation.response.SearchParticipantDetailResponse;
import com.fil.pickple.presentation.response.SearchParticipantProfileResponse;
import com.fil.pickple.presentation.response.SearchTeamParticipantsResponse;
import com.fil.pickple.presentation.response.UpdateMyParticipantProfileResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participants")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<JoinTeamResponse> joinTeam(@ModelAttribute @Valid JoinTeamRequest joinTeamRequest) {
        JoinTeamCommand command = joinTeamRequest.toCommand();

        participantService.joinTeam(command);

        return ResponseEntity.ok(new JoinTeamResponse(true));
    }

    @GetMapping
    public ResponseEntity<SearchTeamParticipantsResponse> searchTeamParticipants(@RequestParam("team") Long teamId){
        SearchTeamParticipantsResult result = participantService.searchTeamParticipants(teamId);

        return ResponseEntity.ok(SearchTeamParticipantsResponse.from(result));
    }

    @GetMapping("/{participant-id}/profile")
    public ResponseEntity<SearchParticipantProfileResponse> searchParticipantProfile(@PathVariable("participant-id") Long participantId) {
        SearchParticipantProfileCommand command = SearchParticipantProfileCommand.of(participantId);

        SearchParticipantProfileResult result = participantService.searchParticipantProfile(command);

        return ResponseEntity.ok(SearchParticipantProfileResponse.from(result));
    }

    @GetMapping("/{participant-id}")
    public ResponseEntity<SearchParticipantDetailResponse> searchParticipantDetail(@PathVariable("participant-id") Long participantId) {
        SearchParticipantDetailCommand command = SearchParticipantDetailCommand.of(participantId);

        SearchParticipantDetailResult result = participantService.searchParticipantDetail(command);

        return ResponseEntity.ok(SearchParticipantDetailResponse.from(result));
    }

    @PatchMapping("/{participant-id}")
    public ResponseEntity<UpdateMyParticipantProfileResponse> editParticipant(
            @PathVariable("participant-id") Long participantId,
            @ModelAttribute @Valid EditParticipantRequest request
    ) {
        EditParticipantCommand command = request.toCommand(participantId);

        EditParticipantResult result = participantService.editParticipant(command);

        return ResponseEntity.ok(UpdateMyParticipantProfileResponse.from(result));
    }

    @DeleteMapping("/{participant-id}")
    public ResponseEntity<DeleteParticipantResponse> deleteParticipant(@PathVariable("participant-id") Long participantId) {
        LeaveTeamCommand command = LeaveTeamCommand.of(participantId);

        participantService.leaveTeam(command);

        return ResponseEntity.ok(new DeleteParticipantResponse(true));
    }
}
