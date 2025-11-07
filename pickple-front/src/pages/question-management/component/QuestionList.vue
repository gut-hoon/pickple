<template>
  <div class="question-list">
    <!-- Empty State -->
    <div v-if="questions.length === 0" class="empty-state">
      <div class="empty-icon">
        <i class="fas fa-question-circle"></i>
      </div>
      <p class="empty-text">아직 등록된 질문이 없습니다</p>
      <p class="empty-subtext">첫 번째 질문을 추가해보세요</p>
    </div>

    <!-- Questions List -->
    <div v-else class="list">
      <Card
        v-for="q in questions"
        :key="q.id"
        :class="{ 'fade-out': q.isDeleting }"
      >
        <div class="item-content">
          <div class="text-area">
            <p class="question-text">{{ q.content }}</p>
            <p class="question-date">{{ formetDateToMessege(q.createdAt) }}</p>
          </div>
          <button class="delete-btn" @click="$emit('delete', q.id)">
            <i class="fas fa-trash"></i>
          </button>
        </div>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { Card } from '@/shared/ui';
import { formetDateToMessege } from '@/shared/lib';
defineProps({ questions: Array });
defineEmits(['delete']);
</script>

<style scoped>
.question-list {
  padding: 0 0 24px;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.item-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  transition: all 0.2s;
  max-height: 60px;
  overflow: hidden;
}

.question-text {
  margin: 0.5em 0;
  font-size: 14px;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  display: -webkit-box;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.5;
  color: #111;
}

.question-date {
  font-size: 12px;
  color: #9ca3af;
  margin-top: 8px;
}

.delete-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #ef4444;
  color: white;
  border: none;
  cursor: pointer;
  transition: background 0.2s;
  margin: auto 0;
}

.delete-btn:hover {
  background: #dc2626;
}

@keyframes fadeOut {
  from { opacity: 1; transform: translateX(0); }
  to { opacity: 0; transform: translateX(100%); }
}
.fade-out {
  animation: fadeOut 0.2s forwards;
}

.empty-state {
  text-align: center;
  padding: 64px 0;
}

.empty-icon {
  width: 64px;
  height: 64px;
  background: #e5e7eb;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.empty-icon i {
  font-size: 24px;
  color: #9ca3af;
}

.empty-text {
  color: #6b7280;
  font-size: 14px;
  margin-bottom: 4px;
}

.empty-subtext {
  color: #9ca3af;
  font-size: 13px;
}
</style>
