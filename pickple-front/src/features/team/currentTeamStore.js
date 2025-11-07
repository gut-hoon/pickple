import { defineStore } from 'pinia';

export const useCurrentTeamStore = defineStore('currentTeam', {
    state: () => ({
        currentTeam: null,
        participant: null,
    }),
    
    actions: {
        enterTeam(team) {
            this.currentTeam = team;
        },

        leftTeam() {
            this.team = null;
            this.participant = null;
        },

        setParticipant(participant){
            this.participant = participant;
        },

        async fetchParticipant() {
            // TODO: API 호출
            await new Promise(resolve => setTimeout(resolve, 1000));

            this.participant = {
                nickname: '박철규',
                avatarImage: '/ProfileMemberAvatar.png',
                backgroundImage: '/ProfileBackground.png',
            };
        },
    },
});
