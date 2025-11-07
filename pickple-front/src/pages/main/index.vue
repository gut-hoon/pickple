<template>
    <LoadingSpinner v-if="isLoading" message="메인 페이지 불러오는 중..." />
    <template v-else>
        <Section title="내 팀" :is-refreshing="isFetchingTeams" @refresh="fetchTeams">
            <div v-if="teams.length === 0" class="empty-message">
                소속된 팀이 없습니다.
            </div>
            <template v-else>
                <TeamItem
                    v-for="team in teams"
                    :key="team.id"
                    :team="team"
                />
            </template>
        </Section>
        <div class="team-actions">
            <button @click="createTeam" class="team-action-button">
                팀 생성
            </button>
            <button @click="joinNewTeam" class="team-action-button">
                팀 참가
            </button>
        </div>

        <Section title="핫보드" :is-refreshing="isFetchingHotboard" @refresh="fetchHotboard">
            <HotboardItem
                icon="👑"
                label="최근 가장 많은 픽을 받은 사용자"
                :item="hotboard.topMembers[0]"
                :itemLabel="hotboard.topMembers[0]?.nickname"
            />
            <HotboardItem
                icon="🔥"
                label="최근 가장 픽이 많이 된 질문"
                :item="hotboard.topQuestions[0]"
                :itemLabel="hotboard.topQuestions[0]?.content"
            />
            <HotboardItem
                icon="👥"
                label="최근 가장 많은 픽을 한 팀"
                :item="hotboard.topTeams[0]"
                :itemLabel="hotboard.topTeams[0]?.name"
            />
        </Section>
    </template>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { useTeamListStore, useHotBoardStore } from '@/features';
import { LoadingSpinner } from '@/shared/ui';
import Section from './component/Section.vue';
import HotboardItem from './component/HotboardItem.vue';
import TeamItem from './component/TeamItem.vue';

const isLoading = ref(true);

const teamListStore = useTeamListStore();
const { teams, isFetchingTeams } = storeToRefs(teamListStore);
const { fetchTeams } = teamListStore;

const hotBoardStore = useHotBoardStore();
const { hotboard, isFetchingHotboard } = storeToRefs(hotBoardStore);
const { fetchHotboard } = hotBoardStore;

const router = useRouter();

// 새 팀 들어가기
const joinNewTeam = () => {
    router.push('/join-team');
};

// 팀 생성 — /create-team으로 이동
const createTeam = () => {
    router.push('/create-team');
};

// 초기 데이터 로드
onMounted(async () => {
    isLoading.value = true;
    try {
        await Promise.all([
            fetchTeams(),
            fetchHotboard()
        ]);
    } catch (error) {
        console.error("초기 데이터 로드 실패:", error);
    } finally {
        isLoading.value = false;
    }
});
</script>
<style scoped>
* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
}

.app-container {
    min-height: 100vh;
    background-color: #F8F9FA;
    font-family: 'Noto Sans KR', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
}

.main-content {
    padding: 0 16px 24px 16px;
}

.team-action-button {
    width: 100%;
    background-color: #B7DCEB;
    color: #5E727B;
    font-weight: 600;
    padding: 16px;
    border-radius: 12px;
    border: none;
    cursor: pointer;
    transition: background-color 0.2s ease-in-out;
    font-size: 16px;
    white-space: nowrap;
    margin-top: 16px;
}

.team-action-button:hover {
    background-color: #DAEFF8;
}

.team-actions {
    display: flex;
    gap: 8px;
    margin-top: 16px;
}

.team-actions button {
    flex: 1;
}

.empty-message {
    text-align: center;
    color: #6B7280;
    padding: 20px 0;
}
</style>
