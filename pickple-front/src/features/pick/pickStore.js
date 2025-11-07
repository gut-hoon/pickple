import { defineStore } from 'pinia';
import { createPick } from '@/entities';

export const usePickStore = defineStore('pick', {
    state: () => ({
        question: null,
        isFetchingQuestion: false
    }),

    actions: {
        async fetchQuestion(teamId) {
            this.isFetchingQuestion = true;
            try {
                const res = await createPick({teamId});

                this.question = res.question;
            } catch (error) {
                console.error("질문 가져오기 실패", error);
                this.question = null;
            } finally {
                this.isFetchingQuestion = false;
            }
        },

        async skip(teamId, questionId) {
            this.isFetchingQuestion = true;
            try {
                const res = await createPick({teamId, questionId});

                this.question = res.question;
            } catch (error) {
                console.error("질문 가져오기 실패", error);
                this.question = null;
            } finally {
                this.isFetchingQuestion = false;
            }
        },

        async pick(teamId, questionId, pickpleId) {
            this.isFetchingQuestion = true;
            try {
                const res = await createPick({teamId, questionId, pickpleId});

                this.question = res.question;
            } catch (error) {
                console.error("질문 가져오기 실패", error);
                this.question = null;
            } finally {
                this.isFetchingQuestion = false;
            }
        }


    }
});