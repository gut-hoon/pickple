import { defineStore } from 'pinia';
import { getQuestionList, createQuestion, deleteQuestion, refreshAllDoneQuestion } from '@/entities';

export const useQuestionListStore = defineStore('questionList', {
    state: () => ({
        questions: [],
        isFetchingQuestions: false,
    }),

    actions: {
        async fetchQuestionList(teamId, memberId) {
            this.isFetchingQuestions = true;
            try {
                const res = await getQuestionList(teamId, memberId);

                this.questions = res.questions;
                this.questions.map(e => e.isDeleting = false)
            } catch (error) {
                console.error('질문 리스트 갸져오기 실패', error);
                this.questions = [];
            } finally {
                this.isFetchingQuestions = false;
            }
        },

        async createQuestion(teamId, content) {
            try {
                const res = await createQuestion(teamId, content);

                this.questions.unshift({
                    id: res.questionId,
                    content: res.content,
                    createdAt: new Date(),
                    isDeleting: false
                });
            } catch (error) {
                console.error('질문 생성 실패', error);
            }
        },

        async deleteQuestion(questionId) {
            try {
                await deleteQuestion(questionId);

                const index = this.questions.findIndex((q) => q.id === questionId);
                if (index !== -1) {
                    this.questions[index].isDeleting = true;
                    setTimeout(() => this.questions.splice(index, 1), 200);
                }
            } catch (error) {
                console.error('질문 삭제 실패', error);
            }
        },

        async refreshAllDoneQuestion(teamId) {
            try {
                await refreshAllDoneQuestion(teamId);
            } catch (error) {
                console.error('질문 새로고침 실패', error);
            }
        }
    }
})
