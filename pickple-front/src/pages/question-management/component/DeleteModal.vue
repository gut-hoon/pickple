<template>
  <div class="modal-overlay">
    <div class="modal">
      <div class="modal-content">
        <div class="modal-icon">
          <i class="fas fa-exclamation-triangle"></i>
        </div>
        <h3 class="modal-title">질문 삭제</h3>
        <p class="modal-message">
          이 질문을 삭제하시겠습니까?<br />
          삭제된 질문은 복구할 수 없습니다.
        </p>
        <div class="modal-buttons">
          <button @click="$emit('cancel')" class="cancel-btn">취소</button>
          <button @click="$emit('confirm')" class="confirm-btn">삭제</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineEmits(['confirm', 'cancel']);
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal {
  background: white;
  border-radius: 12px;
  width: 100%;
  max-width: 360px;
  margin: 0 16px;
}

.modal-content {
  padding: 24px;
  text-align: center;
}

.modal-icon {
  width: 48px;
  height: 48px;
  background: #ef4444;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  margin: 0 auto 16px;
}

.modal-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
}

.modal-message {
  font-size: 14px;
  color: #4b5563;
  margin-bottom: 24px;
  line-height: 1.5;
}

.modal-buttons {
  display: flex;
  gap: 12px;
}

.cancel-btn,
.confirm-btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 500;
}

.cancel-btn {
  background: #e5e7eb;
  color: #374151;
}

.confirm-btn {
  background: #ef4444;
  color: white;
}
</style>
