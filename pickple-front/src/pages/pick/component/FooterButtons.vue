<template>
  <div class="footer">
    <div class="footer-buttons">
      <button @click="$emit('skip')" class="btn btn-skip">건너뛰기</button>
      <button
        @click="$emit('next')"
        :disabled="!hasSelection"
        :class="['btn', 'btn-next', !hasSelection ? 'disabled' : '']"
      >
        다음
      </button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  hasSelection: Boolean
})
</script>

<style scoped>
.footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  border-top: 1px solid #e5e7eb;
  padding: 1rem 1.25rem;
  box-shadow: 0 -2px 6px rgba(0, 0, 0, 0.04);
  z-index: 10;
}

.footer-buttons {
  display: flex;
  gap: 0.75rem;
  max-width: 480px;
  margin: 0 auto;
}

.btn {
  flex: 1;
  padding: 0.8rem 1rem;
  border-radius: 12px;
  font-weight: 600;
  font-size: 1rem;
  transition: all 0.2s ease;
  cursor: pointer;
  border: none;
}

.btn-skip {
  background-color: #f3f4f6;
  color: #374151;
}

.btn-skip:hover {
  background-color: #e5e7eb;
}

.btn-next {
  background-color: #3b82f6;
  color: white;
}

.btn-next:hover {
  background-color: #2563eb;
}

.btn-next.disabled {
  background-color: #e5e7eb;
  color: #9ca3af;
  cursor: not-allowed;
}
</style>
