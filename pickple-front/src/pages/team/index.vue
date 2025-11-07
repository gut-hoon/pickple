<template>
  <div class="team-page-container">
    <!-- 팀 프로필 -->
    <div class="profile-card-wrapper">
      <ProfileCard
        :avatarImage="team.avatar"
        :backgroundImage="team.background"
        :name="team.name"
        :description="team.season"
        :toEdit="`/edit-team/${teamId}/${participantId}`"
      />
    </div>

      <ListCard titleLabel="시즌 TOP 3" titleIconClass="fas fa-chart-bar">
          <DisplayItem
              v-for="(item, index) in teamProfileStore.currentTopQuestions"
              :key="index"
              :label = "item.content"
          />
          <div
            v-if="!teamProfileStore.currentTopQuestions || teamProfileStore.currentTopQuestions.length === 0"
            class="empty-state">
            <i class="fas fa-inbox empty-icon"></i>
            <p class="empty-message">이번 시즌에 픽을 받은 질문이 없습니다</p>
            <p class="empty-message">픽을 하러 가보세요!</p>
          </div>
      </ListCard>

    <!-- 액션 버튼 -->
    <section class="action-buttons">
      <router-link :to="`/question-management/${teamId}/${meStore.id}`" class="action-button">
        <i class="fas fa-plus-circle"></i>
        <span>질문 등록</span>
      </router-link>
      <button @click="handleRefreshAllDoneQuestion" class="action-button">
        <i class="fas fa-sync-alt"></i>
        <span>픽 초기화</span>
      </button>
      <router-link :to="`/team/${teamId}/invite-code`" class="action-button">
        <i class="fas fa-share-alt"></i>
        <span>초대코드</span>
      </router-link>
    </section>

    <!-- 팀원 목록 -->
    <section class="team-members">
      <h2 class="section-title">팀원 목록</h2>
      <div class="member-list">
        <div v-for="member in visibleMembers" :key="member.id" class="member-item">
          <img :src="member.avatarImage" alt="Member Avatar" class="member-avatar" />
          <span class="member-name">{{ member.nickname }}</span>
        </div>
      </div>
      <button v-if="hasMoreMembers" @click="showMoreMembers" class="show-more-button">
        ▼
      </button>
    </section>

    <!-- 픽 하기 버튼 -->
    <footer class="footer">
      <button class="pick-button" @click="goPickPage">픽 하기</button>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ProfileCard, ListCard, DisplayItem } from '@/shared/ui/index.js';
import { useTeamProfileStore, useTeamParticipantStore, useMeStore, useParticipantStore, useQuestionListStore, useCurrentSeasonStore } from '@/features';

const route = useRoute();
const router = useRouter();

const teamProfileStore = useTeamProfileStore();
const teamParticipantStore = useTeamParticipantStore();
const meStore = useMeStore();
const participantProfileStore = useParticipantStore();
const questionListStore = useQuestionListStore();
const currentSeasonStore = useCurrentSeasonStore();

const teamId = route.params.teamId;
const participantId = route.params.participantId;

onMounted(() => {
  currentSeasonStore.fetchCurrentSeason();
  teamProfileStore.fetchTeamProfile(teamId);
  teamParticipantStore.fetchTeamParticipants(teamId);
  meStore.fetchMe();
  participantProfileStore.fetchParticipant(participantId);
});

const team = computed(() => ({
  name: teamProfileStore.teamName,
  season: currentSeasonStore.name,
  avatar: teamProfileStore.teamAvatarImage,
  background: teamProfileStore.teamBackgroundImage,
}));

const visibleCount = ref(3);
const visibleMembers = computed(() => teamParticipantStore.participants.slice(0, visibleCount.value));
const hasMoreMembers = computed(() => visibleCount.value < teamParticipantStore.participants.length);
const showMoreMembers = () => {
  visibleCount.value += 3;
};

const handleRefreshAllDoneQuestion = async () => {
  try {
    await questionListStore.refreshAllDoneQuestion(teamId);
  } catch (error) {
    console.log('err', error);
  }
};

const goPickPage = () => {
  router.push(`/pick/${teamId}/${participantId}`);
}
</script>

<style scoped>
.team-page-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding-bottom: 6rem;
}

.profile-card-wrapper {
  position: relative;
}

.profile-action-button {
  position: absolute;
  top: 12px;
  right: 56px; /* 12px (gap) + 36px (edit button) + 8px (gap) */
  background-color: rgba(255, 255, 255, 0.8);
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-dark);
  box-shadow: var(--shadow);
  transition: transform 0.2s;
  font-size: 14px;
  z-index: 2;
}

.section-title {
  font-size: 1.1rem;
  font-weight: bold;
  margin: 0.5rem 0;
}

.action-buttons {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0.75rem;
  background-color: var(--white);
  padding: 1rem;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
}

.action-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 0;
  border: none;
  background-color: transparent;
  color: var(--primary-dark);
  border-radius: var(--border-radius);
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.action-button:hover {
  background-color: var(--gray);
}

.action-button i {
  font-size: 1.5rem;
  margin-bottom: 0.25rem;
}

.team-members {
  background-color: var(--white);
  padding: 1rem;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
}

.member-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.member-item {
  display: flex;
  align-items: center;
  background-color: var(--light);
  padding: 0.75rem;
  border-radius: var(--border-radius);
}

.member-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 1rem;
}

.member-name {
  font-size: 1rem;
}

.show-more-button {
  width: 100%;
  padding: 0.75rem;
  margin-top: 0.75rem;
  border: none;
  background-color: var(--gray);
  color: var(--primary-dark);
  border-radius: var(--border-radius);
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.show-more-button:hover {
  background-color: #e0e0e0;
}

.footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  max-width: 480px;
  margin: 0 auto;
  padding: 1rem;
  background-color: var(--white);
  box-shadow: 0 -4px 12px rgba(0, 0, 0, 0.05);
  z-index: 10;
}

.pick-button {
  width: 100%;
  padding: 1rem;
  border: none;
  background-color: var(--primary);
  color: var(--white);
  border-radius: var(--border-radius);
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.2s;
}

.pick-button:hover {
  background-color: var(--primary-dark);
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
