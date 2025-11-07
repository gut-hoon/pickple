<template>
  <div class="no-question-wrap">
    <div class="icon-wrapper">
      <i class="fas fa-info-circle icon-blue"></i>
    </div>

    <h2 class="main-message">표시할 질문이 없습니다</h2>
    <p class="sub-message">
      모든 질문에 응답했어요.<br />
      같은 질문에 새로 응답하시겠어요?
    </p>

    <div class="button-group">
      <!-- 새로 응답하기: 기본 파란색 버튼 -->
      <event-button
        label="새로 응답하기"
        @click="$emit('refresh')"
      />

      <!-- 팀 메인으로 돌아가기: secondary 스타일 -->
      <event-button
        label="팀 메인으로 돌아가기"
        class="back-button"
        @click="$emit('goToTeamMain')"
      />
    </div>
  </div>
</template>

<script setup>
import { EventButton } from '@/shared/ui'
</script>

<style scoped>
.no-question-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  margin: 10rem auto;
}

.icon-wrapper {
  width: 64px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background-color: #e0f2fe;
  margin-bottom: 1rem;
}

.icon-blue {
  color: #3b82f6;
  font-size: 2rem;
}

.main-message {
  font-size: 1.3rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.5rem;
}

.sub-message {
  font-size: 0.95rem;
  font-weight: 400;
  color: #6b7280;
  line-height: 1.4;
  margin-bottom: 1.5rem;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  width: 100%;
  max-width: 280px;
}

/* ✅ back-button: event-button 확장용 */
.back-button {
  background-color: #e5e7eb !important; /* gray-200 */
  color: #374151 !important; /* gray-700 */
  border: 1px solid #d1d5db !important;
  transition: all 0.25s ease;
}

.back-button:hover:not(:disabled) {
  background-color: #d1d5db !important; /* gray-300 */
  color: #111827 !important; /* gray-900 */
}
</style>
