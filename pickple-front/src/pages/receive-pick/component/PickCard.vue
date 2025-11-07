<template>
  <Card>
    <div class="pick-card">
      <img
        :src="pick.teamAvatarImage"
        :alt="pick"
        class="team-avatar"
      >
      <p class="pick-content">{{ pick.content }}</p>
    </div>
  </Card>
</template>

<script setup>
import { Card } from '@/shared/ui';

defineProps({
  pick: {
    type: Object,
    required: true
  }
});
</script>

<style scoped>
.pick-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.25rem;
}

.team-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.pick-content {
  line-height: 1.6;
  margin: 0;

  color: var(--black);
  font-size: 1rem;
  word-wrap: break-word;
  white-space: normal;
  flex: 1;
}
</style>
