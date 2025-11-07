<template>
  <Card class="member-card-container">
    <div class="card-header">
      <i class="fas fa-users icon-purple"></i>
      <h3>팀원 선택</h3>
    </div>

    <div class="member-grid">
      <button
        v-for="participant in participants"
        :key="participant.id"
        @click="$emit('select', participant.id)"
        :class="['member-card', selectedParticipant === participant.id ? 'selected' : '']"
      >
        <div class="avatar-wrapper">
          <img :src="participant.avatarImage" :alt="participant.nickname" class="avatar" />
          <div v-if="selectedParticipant === participant.id" class="check-badge">
            <i class="fas fa-check"></i>
          </div>
        </div>
        <span class="nickname">{{ participant.nickname }}</span>
      </button>
    </div>
  </Card>
</template>

<script setup>
import { Card } from '@/shared/ui'
defineProps({
  participants: Array,
  selectedParticipant: Number
})
</script>

<style scoped>
.member-card-container {
  padding: 1.3rem;
  margin-bottom: 1rem;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.75rem;
}

.member-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
  gap: 1rem;
  margin-top: 0.5rem;
}

.member-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem;
  border-radius: 12px;
  background-color: #f9fafb;
  border: 2px solid transparent;
  transition: all 0.25s ease;
  cursor: pointer;
}

.member-card:hover {
  background-color: #f3f4f6;
  transform: translateY(-2px);
}

.member-card.selected {
  background-color: #eff6ff;
  border-color: #3b82f6;
  transform: scale(0.98);
}

.avatar-wrapper {
  position: relative;
}

.avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  object-fit: cover;
}

.check-badge {
  position: absolute;
  top: -6px;
  right: -6px;
  width: 22px;
  height: 22px;
  background-color: #3b82f6;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.check-badge i {
  color: white;
  font-size: 0.75rem;
}

.nickname {
  font-size: 0.9rem;
  font-weight: 500;
  color: #111827;
  text-align: center;
  word-break: break-word;
}

.icon-purple {
  color: #a855f7;
  font-size: 1.25rem;
}
</style>
