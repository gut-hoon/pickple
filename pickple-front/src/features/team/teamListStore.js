import { defineStore } from 'pinia';
import { getMyTeams } from '@/entities';

export const useTeamListStore = defineStore('teamList', {
    state: () => ({
        teams: [],
        isFetchingTeams: false,
    }),
    
    actions: {
        async fetchTeams() {
            this.isFetchingTeams = true;
            try {
                const data = await getMyTeams();
                this.teams = data.teams;
            } catch (error) {
                console.error('팀 목록 가져오기 실패:', error);
            } finally {
                this.isFetchingTeams = false;
            }
        },

        async findTeam(teamId) {
            // 팀 목록이 없으면 먼저 가져오기
            if (this.teams.length === 0) {
                await this.fetchTeams();
            }

            // 팀 ID로 팀 찾기
            const team = this.teams.find(t => t.id === Number(teamId));

            if (!team) {
                console.error(`팀을 찾을 수 없습니다. (ID: ${teamId})`);
                return null;
            }

            return team;
        }
    },
});