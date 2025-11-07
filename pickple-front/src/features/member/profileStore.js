import { defineStore } from 'pinia';
import { getMemberProfile } from '@/entities';

export const useMemberProfileStore = defineStore('memberProfile', {
    state: () => ({
        id: null,

        // 프로필
        nickname: null,
        avatarImage: null,
        backgroundImage: null,

        // 픽 통계
        seasonTopQuestions: [],
        totalTopQuestions: []
    }),

    actions: {
        async fetchProfile(memberId) {
            const data = await getMemberProfile(memberId);

            this.nickname = data.nickname;
            this.avatarImage = data.profileImage;
            this.backgroundImage = data.backgroundImage;
            this.seasonTopQuestions = data.seasonTopQuestions;
            this.totalTopQuestions = data.totalTopQuestions;
        },
    },
})