import { defineStore } from 'pinia';
import { getTeamProfile, patchTeam, deleteParticipant } from '@/entities';

export const useTeamProfileStore = defineStore('teamProfile', {
    state: () => ({
        teamId : null,
        teamName: '',
        teamAvatarImage: '',
        teamBackgroundImage: '',
        currentTopQuestions: [],
        isFetchingTeamProfile: false,
    }),
    actions: {
        async fetchTeamProfile(teamId) {
            this.isFetchingTeamProfile = true;
            try {
                const data = await getTeamProfile(teamId);
                this.teamName = data.teamName;
                this.teamAvatarImage = data.teamAvatarImage;
                this.teamBackgroundImage = data.teamBackgroundImage;
                this.currentTopQuestions = data.currentTopQuestions;
            } catch (error) {
                console.error('팀 프로필 가져오기 실패:', error);
            } finally {
                this.isFetchingTeamProfile = false;
            }
        },

        async tryEditTeam(editedInformation) {
            const formData = new FormData();

            if (editedInformation.teamName !== this.teamName) {
                formData.append('teamName', editedInformation.name);
            }

            if (editedInformation.avatarImage) {
                const avatarBlob = await fetch(editedInformation.avatarImage).then(res => res.blob());
                formData.append('teamAvatarImage', avatarBlob, 'teamAvatar.png');
            }

            if (editedInformation.backgroundImage) {
                const backgroundBlob = await fetch(editedInformation.backgroundImage).then(res => res.blob());
                formData.append('teamBackgroundImage', backgroundBlob, 'teamBackground.png');
            }

            if (editedInformation.removeAvatarImage) {
                formData.append('removeAvatarImage', 'true');
            }
            
            if (editedInformation.removeBackgroundImage) {
                formData.append('removeBackgroundImage', 'true');
            }

            const response = await patchTeam(editedInformation.teamId, formData);

            this.teamName = response.teamName;
            this.teamAvatarImage = response.teamAvatarImage;
            this.teamBackgroundImage = response.teamBackgroundImage;

            return response;
        },

        async leaveTeam(participantId) {
            try {
                await deleteParticipant(participantId);
            } catch (error) {
                console.error('팀 나가기 실패:', error);
                throw error;
            }
        }
    },
});