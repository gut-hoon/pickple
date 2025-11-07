import { defineStore } from "pinia";
import { getInvitationCode } from '@/entities'

export const useInvitationCodeStore = defineStore("invitationCode", {
  state: () => ({
    invitationCode: null,
  }),
    actions: {
        async fetchInvitationCode(teamId) {
            try {
                const data = await getInvitationCode(teamId);
                this.invitationCode = data.invitationCode;
            } catch (error) {
                console.error('초대 코드 가져오기 실패:', error);
            }
        },
    },
});