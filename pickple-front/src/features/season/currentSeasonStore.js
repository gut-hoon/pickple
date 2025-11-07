import { defineStore } from 'pinia';
import { getCurrentSeason } from '@/entities';

export const useCurrentSeasonStore = defineStore('currentSeason', {
    state: () => ({
        id : null,
        name: null,
        start: null,
        end: null,
        isFetchingSeason: false,
    }),
    
    actions: {
        async fetchCurrentSeason() {
            this.isFetchingSeason = true;
            try {
                const seasonData =  await getCurrentSeason();
                
                this.id = seasonData.currentSeason.id;
                this.name = seasonData.currentSeason.name;
                this.start = seasonData.currentSeason.start;
                this.end = seasonData.currentSeason.end;
                this.isFetchingSeason = false;
            } catch (error) {
                console.error('현재 시즌 가져오기 실패:', error);
                this.isFetchingSeason = false;
            }
        }
    },
});