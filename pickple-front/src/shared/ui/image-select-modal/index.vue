<template>
  <div v-if="isOpen" class="modal-overlay" @click="handleOverlayClick">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h3>{{ title }}</h3>
        <button class="close-button" @click="close">
          <i class="fas fa-times"></i>
        </button>
      </div>

      <div class="modal-body">
        <button
          class="option-button"
          @click="handleUpload"
        >
          <i class="fas fa-upload"></i>
          <span>사진 업로드</span>
        </button>

        <button
          v-if="showDefault"
          class="option-button"
          @click="handleDefaultImage"
        >
          <i class="fas fa-image"></i>
          <span>기본 사진으로 변경</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: '이미지 선택'
  },
  showDefault: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['close', 'upload', 'default']);

const close = () => {
  emit('close');
};

const handleOverlayClick = () => {
  close();
};

const handleUpload = () => {
  emit('upload');
  close();
};

const handleDefaultImage = () => {
  emit('default');
  close();
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: var(--white);
  border-radius: 12px;
  width: 90%;
  max-width: 400px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.5rem;
  border-bottom: 1px solid var(--gray-200, #f0f0f0);
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--gray-800, #333);
}

.close-button {
  background: none;
  border: none;
  color: var(--gray-600, #666);
  font-size: 20px;
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.close-button:hover {
  background-color: var(--gray-100, #f5f5f5);
}

.modal-body {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.option-button {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 1.25rem;
  background: var(--white);
  border: 1px solid var(--gray-300, #ddd);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 16px;
  color: var(--gray-700, #444);
  text-align: left;
}

.option-button:hover {
  background-color: var(--gray-50, #fafafa);
  border-color: var(--primary);
  color: var(--primary);
}

.option-button i {
  font-size: 20px;
  width: 24px;
  text-align: center;
}

.option-button span {
  flex: 1;
  font-weight: 500;
}
</style>
