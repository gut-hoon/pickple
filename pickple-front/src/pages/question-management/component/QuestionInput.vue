<template>
  <div class="input-section">
    <div class="input-row">
      <input v-model="text" type="text" placeholder="질문을 입력하세요" class="question-input" @keypress.enter="submit" />
      <button @click="submit" :disabled="!text.trim()" class="add-btn">
        추가
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
const emit = defineEmits(['add']);
const text = ref("");
const submit = () => {
  if (!text.value.trim()) return;
  emit('add', text.value.trim());
  text.value = "";
};
</script>

<style scoped>
.input-section {
  background: white;
  padding: 24px 16px;
  margin-bottom: 0.5em;
}

.input-row {
  display: flex;
  gap: 12px;
}

.question-input {
  flex: 1;
  padding: 12px 16px;
  background: #f3f4f6;
  border-radius: 12px;
  border: none;
  font-size: 14px;
  color: #111;
}

.question-input:focus {
  outline: 2px solid #007aff;
}

.add-btn {
  padding: 12px 24px;
  background: #007aff;
  color: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 500;
  transition: opacity 0.2s;
}

.add-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
