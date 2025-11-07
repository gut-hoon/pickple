<template>
  <div class="invite-code-page">
    <h1 class="title">초대코드</h1>
    <div v-if="isLoading" class="loading-container">
      <LoadingSpinner />
    </div>
    <div v-else class="invite-code-container">
      <p class="description">팀원을 초대하기 위한 코드입니다. 아래 코드를 복사하여 공유해주세요.</p>
      <div class="code-box">
        <span class="code">{{ invitationCode }}</span>
        <button @click="copyToClipboard" class="copy-button">
          <i class="fas fa-copy"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useInvitationCodeStore } from '@/features';
import { LoadingSpinner } from '@/shared/ui';

const route = useRoute();
const invitationCode = ref('');
const isLoading = ref(true);
const invitationCodeStore = useInvitationCodeStore();
onMounted(async () => {
  const teamId = route.params.teamId;
  await invitationCodeStore.fetchInvitationCode(teamId);
  invitationCode.value = invitationCodeStore.invitationCode;
  isLoading.value = false;
});

const copyToClipboard = () => {
  navigator.clipboard.writeText(invitationCode.value).then(() => {
    alert('초대코드가 복사되었습니다.');
  });
};
</script>

<style scoped>
.invite-code-page {
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 120px);
  gap: 2rem;
}

.title {
  font-size: 1.8rem;
  font-weight: bold;
}

.loading-container {
  padding: 4rem 0;
}

.invite-code-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
  width: 100%;
  max-width: 400px;
}

.description {
  font-size: 1rem;
  color: var(--gray-600);
  text-align: center;
  line-height: 1.6;
}

.code-box {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 1rem 1.5rem;
  background-color: var(--gray-100);
  border-radius: var(--border-radius-lg);
  border: 1px solid var(--gray-200);
}

.code {
  font-size: 1.5rem;
  font-weight: 500;
  color: var(--primary-dark);
  letter-spacing: 2px;
}

.copy-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: var(--gray-500);
  cursor: pointer;
  transition: color 0.2s;
}

.copy-button:hover {
  color: var(--primary);
}
</style>
