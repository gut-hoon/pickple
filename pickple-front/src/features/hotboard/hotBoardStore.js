import { defineStore } from 'pinia';
import { getHotboard } from '@/entities';

export const useHotBoardStore = defineStore('hotBoard', {
    state: () => ({
        hotboard: {
            topMembers: [],
            topQuestions: [],
            topTeams: [],
        },
        isFetchingHotboard: false,
    }),
    actions: {
        async fetchHotboard() {
            this.isFetchingHotboard = true;
            try {
                const data = await getHotboard();
                this.hotboard = data;
            } catch (error) {
                console.error('핫보드 가져오기 실패:', error);
            } finally {
                this.isFetchingHotboard = false;
            }
        },
    },
});