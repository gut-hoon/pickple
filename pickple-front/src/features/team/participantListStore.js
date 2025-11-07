import { defineStore } from 'pinia'
import { getTeamParticipants } from '@/entities'

export const useTeamParticipantStore = defineStore('teamParticipant', {
  state: () => ({
    participants: [],
    isFetchingParticipants: false,
  }),
  actions: {
    async fetchTeamParticipants(teamId) {
      this.isFetchingParticipants = true
      try {
        const res = await getTeamParticipants(teamId)
        
        this.participants = res.participants
      } catch (error) {
        console.error('팀 멤버 가져오기 실패:', error)
        this.participants = []
      } finally {
        this.isFetchingParticipants = false
      }
    },

    reset() {
      this.participants = []
      this.isFetchingParticipants = false
    },
  },
})
