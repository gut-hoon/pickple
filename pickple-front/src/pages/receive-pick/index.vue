<template>
  <div class="receive-pick-page">
    <!-- 팀 선택 드롭다운 -->
    <div class="filter-section">
      <Dropdown v-model="selectedTeam" :items="teams" />
    </div>

    <!-- 질문 목록 -->
    <div class="content-container">
      <!-- 로딩 중 -->
      <LoadingSpinner v-if="isLoading" message="질문을 불러오는 중..." />

      <!-- 픽이 없는 경우 -->
      <div v-else-if="receivePicks.length === 0" class="empty-state">
        <i class="fas fa-question-circle empty-icon"></i>
        <p class="empty-text">받은 픽이 없습니다</p>
      </div>

      <!-- 날짜별 그룹 -->
      <div v-else class="picks-container">
        <div
          v-for="(group, date) in groupedPicksByDate"
          :key="date"
          class="date-group"
        >
          <div class="date-header">
            <h2 class="date-title">{{ formatDate(date) }}</h2>
          </div>
          <div class="picks-list">
            <PickCard
              v-for="pick in group"
              :key="pick.id"
              :pick="pick"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useReceivePickStore } from '@/features';
import { Dropdown, LoadingSpinner } from '@/shared/ui';
import { formatDate, extractDateString } from '@/shared/lib';
import PickCard from './component/PickCard.vue';

const DEFAULT_SELECT = -1; // 전체 선택 ID

// Store
const store = useReceivePickStore();

// 상태
const selectedTeam = ref(DEFAULT_SELECT);
const isInitialLoad = ref(true);

const teams = computed(() => store.teams);
const receivePicks = computed(() => store.picks);
const isLoading = computed(() => store.loading || isInitialLoad.value);

const groupedPicksByDate = computed(() => {
  const groups = {};

  receivePicks.value.forEach(pick => {
    const dateString = extractDateString(pick.createdAt);
    if (!groups[dateString]) {
      groups[dateString] = [];
    }
    groups[dateString].push(pick);
  });

  return groups;
});

// 팀 선택 변경 시 데이터 조회
watch(selectedTeam, (newTeamId) => {
  store.fetchPicks(newTeamId);
});

// TODO: 무한 스크롤


// 초기 데이터 로드
onMounted(async () => {
  await store.fetchTeams();
  await store.fetchPicks(DEFAULT_SELECT);
  isInitialLoad.value = false;
});
</script>

<style scoped>
.receive-pick-page {
  min-height: 100vh;
  background-color: #F9FAFB;
}

/* 필터 섹션 */
.filter-section {
  padding: 0;
}

/* 컨텐츠 컨테이너 */
.content-container {
  padding-top: 1.25rem;
}

.picks-container {
  display: flex;
  flex-direction: column;
  gap: 2.5rem;
  max-width: 768px;
  margin: 0 auto;
}

/* 빈 상태 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 5rem 1rem;
}

.empty-icon {
  font-size: 3rem;
  color: #D1D5DB;
  margin-bottom: 1rem;
}

.empty-text {
  color: #6B7280;
  text-align: center;
  font-size: 0.9375rem;
  margin: 0;
}

/* 날짜 그룹 */
.date-header {
  margin-bottom: 1rem;
  padding-left: 0.25rem;
}

.date-title {
  font-size: 0.9375rem;
  font-weight: 700;
  color: var(--primary-dark);
  margin: 0;
  letter-spacing: -0.01em;
}

/* 질문 목록 */
.picks-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
</style>