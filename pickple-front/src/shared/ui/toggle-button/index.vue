<!--
  ToggleButton 컴포넌트
  
  Props:
    - modelValue (Boolean, default: false): 토글 버튼의 ON/OFF 상태
    - id (String, required): input과 label을 연결하기 위한 고유 식별자

  Emits:
    - update:modelValue: 토글 상태가 변경될 때 발생

  사용 예시:
    <script setup>
    import { ref } from 'vue';
    import { ToggleButton } from '@/shared/ui';

    const isEnabled = ref(false);
    </script>

    <template>
      <div>
        <label>알림 설정</label>
        <ToggleButton v-model="isEnabled" id="notification-toggle" />
      </div>
    </template>
-->

<template>
  <div class="toggle-switch">
    <input
      :id="id"
      v-model="internalValue"
      type="checkbox"
      class="toggle-input"
      @change="handleChange"
    />
    <label :for="id" class="toggle-label"></label>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  id: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['update:modelValue']);

const internalValue = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit('update:modelValue', value);
  }
});

const handleChange = () => {
  emit('update:modelValue', internalValue.value);
};
</script>

<style scoped>
.toggle-switch {
  position: relative;
}

.toggle-input {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-label {
  display: block;
  width: 44px;
  height: 24px;
  background-color: var(--gray-300, #e5e7eb);
  border-radius: 12px;
  position: relative;
  transition: background-color 0.2s;
  cursor: pointer;
}

.toggle-label::after {
  content: "";
  position: absolute;
  top: 2px;
  left: 2px;
  width: 20px;
  height: 20px;
  background-color: var(--white);
  border-radius: 50%;
  transition: transform 0.2s;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.toggle-input:checked + .toggle-label {
  background-color: var(--success, #4caf50);
}

.toggle-input:checked + .toggle-label::after {
  transform: translateX(20px);
}
</style>
