import { defineStore } from 'pinia';
import { getParticipantProfile, getParticipant, patchParticipant } from '@/entities';

export const useParticipantStore = defineStore('participantProfileStore', {
    state: () => ({
        id: null,
        memberId: null,
        teamId: null,
        teamName: '',

        // 참가자 정보
        nickname: '',
        avatarImage: '',
        backgroundImage: '',

        // 통계 정보
        seasonTopQuestions: [],
        totalTopQuestions: [],
    }),
    
    actions: {
        async fetchParticipantProfile(participantId) {
            const data = await getParticipantProfile(participantId);

            this.id = participantId;
            this.memberId = data.memberId;
            this.teamId = data.teamId;
            this.teamName = data.teamName;

            this.nickname = data.nickname;
            this.avatarImage = data.avatarImage;
            this.backgroundImage = data.backgroundImage;

            this.seasonTopQuestions = data.seasonQuestions;
            this.totalTopQuestions = data.totalQuestions;
        },

        async fetchParticipant(participantId) {
            const data = await getParticipant(participantId);

            this.id = data.participantId;
            this.teamId = data.teamId;
            this.teamName = data.teamName;
            this.nickname = data.nickname;
            this.avatarImage = data.avatarImage;
            this.backgroundImage = data.backgroundImage;
        },

        async tryEditParticipant(editedInfo) {
            const formData = new FormData();

            if (editedInfo.nickname !== this.nickname) {
                formData.append('nickname', editedInfo.nickname);
            }

            if (editedInfo.avatarImage?.startsWith('data:image/')) {
                const avatarBlob = await fetch(editedInfo.avatarImage).then(res => res.blob());
                formData.append('avatarImage', avatarBlob, 'avatar.png');
            }
            if (editedInfo.backgroundImage?.startsWith('data:image/')) {
                const backgroundBlob = await fetch(editedInfo.backgroundImage).then(res => res.blob());
                formData.append('backgroundImage', backgroundBlob, 'background.png');
            }

            if (editedInfo.removeAvatarImage) {
                formData.append('removeAvatarImage', 'true');
            }
            if (editedInfo.removeBackgroundImage) {
                formData.append('removeBackgroundImage', 'true');
            }

            await patchParticipant(this.id, formData);
        },
    },
});