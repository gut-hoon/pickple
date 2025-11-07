<template>
  <section class="section">
    <Card>
      <div class="section-card-content">
        <div class="section-header">
          <h2 class="section-title">{{ title }}</h2>
          <button @click="$emit('refresh')" class="refresh-button" :disabled="isRefreshing">
            <i class="fas fa-sync-alt" :class="{ 'animate-spin': isRefreshing }"></i>
          </button>
        </div>
        <LoadingSpinner v-if="isRefreshing" :message="`${title} 불러오는 중...`" />
        <slot v-else></slot>
      </div>
    </Card>
  </section>
</template>

<script setup>
import { Card, LoadingSpinner } from '@/shared/ui';

defineProps({
  title: {
    type: String,
    required: true,
  },
  isRefreshing: {
    type: Boolean,
    default: false,
  }
});

defineEmits(['refresh']);
</script>

<style scoped>
.section {
    margin-top: 32px;
}

.section-card-content {
  padding: 20px;
  min-height: 100px; /* 로딩 시 높이 유지를 위해 추가 */
}

.section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;
}

.section-title {
    font-size: 20px;
    font-weight: 600;
    color: #050505;
}

.refresh-button {
    padding: 8px;
    border-radius: 50%;
    background-color: #DAEFF8;
    border: none;
    cursor: pointer;
    transition: background-color 0.2s ease-in-out;
    display: flex;
    align-items: center;
    justify-content: center;
    white-space: nowrap;
}

.refresh-button:hover {
    background-color: #B7DCEB;
}

.refresh-button:disabled {
    cursor: not-allowed;
    opacity: 0.6;
}

.refresh-button i {
    color: #5E727B;
    font-size: 14px;
}

@keyframes spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
}

.animate-spin {
    animation: spin 1s linear infinite;
}
</style>