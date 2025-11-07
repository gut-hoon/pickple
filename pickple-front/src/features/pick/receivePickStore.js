import { defineStore } from 'pinia'

const mockPicks = [
  {
    id: 1,
    content: '새로운 기능 개발 시 사용자 경험을 어떻게 개선할 수 있을까요?',
    teamId: 1,
    teamAvatarImage: '/ProfileMemberAvatar.png',
    createdAt: '2025-10-28T14:30:00Z'
  },
  {
    id: 2,
    content: '코드 리뷰 프로세스를 더 효율적으로 만드는 방법은 무엇인가요?',
    teamId: 2,
    teamAvatarImage: '/ProfileMemberAvatar.png',
    createdAt: '2025-10-28T13:15:00Z'
  },
  {
    id: 3,
    content: '브랜드 인지도를 높이기 위한 마케팅 전략을 어떻게 세워야 할까요?',
    teamId: 1,
    teamAvatarImage: '/ProfileMemberAvatar.png',
    createdAt: '2025-10-28T11:45:00Z'
  },
  {
    id: 4,
    content: '사용자 피드백을 제품 개발에 어떻게 반영할 수 있을까요?',
    teamId: 4,
    teamAvatarImage: '/ProfileMemberAvatar.png',
    createdAt: '2025-10-27T16:20:00Z'
  },
  {
    id: 5,
    content: '모바일 앱의 접근성을 개선하는 방법은 무엇인가요?',
    teamId: 1,
    teamAvatarImage: '/ProfileMemberAvatar.png',
    createdAt: '2025-10-27T14:10:00Z'
  },
  {
    id: 6,
    content: '데이터베이스 성능 최적화를 위한 방안을 제안해주세요.',
    teamId: 2,
    teamAvatarImage: '/ProfileMemberAvatar.png',
    createdAt: '2025-10-26T15:30:00Z'
  }
];

const mockTeams = [
  { id: -1, name: '전체' },
  { id: 1, name: '디자인팀' },
  { id: 2, name: '개발팀' },
  { id: 3, name: '마케팅팀' },
  { id: 4, name: '기획팀' }
];

export const useReceivePickStore = defineStore('receivePick', {
    state: () => ({
        picks: [],
        teams: [],
        loading: false,
        error: null
    }),

    actions: {
        async fetchTeams() {
            try {
                // TODO: API 호출

                await new Promise(resolve => setTimeout(resolve, 200));
                this.teams = mockTeams;
            } catch (error) {
                console.error('Failed to fetch teams:', error);
                this.teams = [];
            }
        },

        async fetchPicks(teamId = -1, lastCreatedAt = null) {
            this.loading = true;
            this.error = null;

            try {
                // TODO: API 호출
                // const response = await api.getReceivePicks(teamId);
                // this.picks = response.data;

                // Mock API 응답 시뮬레이션 (네트워크 지연)
                await new Promise(resolve => setTimeout(resolve, 300));

                // 팀 ID로 필터링된 데이터 반환 (서버에서 필터링된 것처럼)
                if (teamId === -1) {
                    this.picks = mockPicks;
                } else {
                    this.picks = mockPicks.filter(q => q.teamId === teamId);
                }
            } catch (error) {
                console.error('Failed to fetch picks:', error);
                this.error = error.message || 'Failed to fetch picks';
                this.picks = [];
            } finally {
                this.loading = false;
            }
        }
    },
})