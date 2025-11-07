<template>
  <div class="profile-container">
    <!-- 로딩 중 -->
    <div v-if="isLoading" class="loading-wrapper">
      <LoadingSpinner message="프로필을 불러오는 중..." />
    </div>

    <!-- 데이터 로드 완료 -->
    <div v-else class="profile-card-container">
      <!-- 프로필 카드 -->
      <ProfileCard
        :avatarImage="profileStore.avatarImage"
        :backgroundImage="profileStore.backgroundImage"
        :name="profileStore.nickname"
        :toEdit="isMine ? editPage : null"
        :description="profileDescription"
      />

      <ListCard titleLabel="시즌 TOP 3" titleIconClass="fas fa-chart-bar">
          <DisplayItem
              v-for="(item, index) in profileStore.seasonTopQuestions"
              :key="index"
              :leftItem="item.icon"
              :label="item.content"
              :rightItem="item.count"
          />
          <div
            v-if="!profileStore.seasonTopQuestions || profileStore.seasonTopQuestions.length === 0"
            class="empty-state">
            <i class="fas fa-inbox empty-icon"></i>
            <p class="empty-message">이번 시즌에 받은 픽이 없습니다</p>
          </div>
      </ListCard>

      <ListCard titleLabel="전 시즌 TOP 3" titleIconClass="fas fa-chart-bar">
          <DisplayItem
              v-for="(item, index) in profileStore.totalTopQuestions"
              :key="index"
              :label="item.content"
          />
          <div
            v-if="!profileStore.totalTopQuestions || profileStore.totalTopQuestions.length === 0"
            class="empty-state">
            <i class="fas fa-inbox empty-icon"></i>
            <p class="empty-message">받은 픽이 없습니다</p>
          </div>
      </ListCard>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useMeStore, useMemberProfileStore } from '@/features';
import { ProfileCard, ListCard, DisplayItem, LoadingSpinner } from '@/shared/ui';

/* ========================================
 * 상태 관리
 * ======================================== */
const route = useRoute();

const meStore = useMeStore();
const profileStore = useMemberProfileStore();

const isLoading = ref(true);
const isMine = ref(false);

const editPage = "/edit-member";
const profileDescription = ref('13회 픽 되었습니다!');

/* ========================================
 * 라이프사이클
 * ======================================== */
onMounted(async () => {
  try {
    const targetId = route.params.memberId;

    await profileStore.fetchProfile(targetId);
    const myId = await meStore.getMyId();

    isMine.value = myId == targetId;
  } finally {
    isLoading.value = false;
  }
})
</script>

<style scoped>
.loading-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
}

.profile-container {
  max-width: 100%;
  margin: 0 auto;

  display: flex;
  flex-direction: column;
  gap: 16px;

  box-sizing: border-box;
}

.profile-card-container {
  max-width: 100%;
  margin: 0 auto;

  display: flex;
  flex-direction: column;
  gap: 16px;

  box-sizing: border-box;
}

@media (max-width: 320px) {
  .profile-container {
    padding: 0 12px;
  }
}

.empty-state {
  text-align: center;
  padding: 32px 16px;
}

.empty-icon {
  font-size: 30x;
  color: var(--medium-gray);
  margin-bottom: 12px;
}

.empty-message {
  color: var(--medium-gray);
  font-size: 14px;
  margin: 0;
}
</style>
