<!--
  Form 컴포넌트

  설명:
    Label(선택)과 Input을 포함하는 폼 컴포넌트입니다.
    v-model을 통한 양방향 바인딩을 지원합니다.

  Props:
    - label (String, optional): 입력 필드의 레이블 (없으면 label 미표시)
    - modelValue (String | Number | Date): 입력 값
    - type (String, default: 'text'): input type (text, date, email 등)
    - placeholder (String): placeholder 텍스트
    - error (String, optional): 에러 메시지 (있으면 에러 상태로 표시)

  Emits:
    - update:modelValue: 입력 값이 변경될 때 발생

  사용 예시:
    <script setup>
    import { ref } from 'vue';
    import { Form } from '@/shared/ui';

    const name = ref('');
    </script>

    <template>
      <Form
        label="이메일"
        v-model="email"
        type="email"
        placeholder="이메일을 입력하세요"
        error="올바른 이메일 형식이 아닙니다"
      />
    </template>
-->

<template>
  <div class="form">
    <label v-if="label" class="form-label">{{ label }}</label>
    <input
      :value="modelValue"
      :type="type"
      :placeholder="placeholder"
      :class="['form-input', { 'form-input--error': error }]"
      @input="$emit('update:modelValue', $event.target.value)"
    />
    <span v-if="error" class="form-error">{{ error }}</span>
  </div>
</template>

<script setup>
defineProps({
  label: {
    type: String,
    default: ''
  },
  modelValue: {
    type: [String, Number, Date],
    default: ''
  },
  type: {
    type: String,
    default: 'text'
  },
  placeholder: {
    type: String,
    default: ''
  },
  error: {
    type: String,
    default: ''
  }
});

defineEmits(['update:modelValue']);
</script>

<style scoped>
.form {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-size: 16px;
  font-weight: 600;
  color: var(--black);
}

.form-input {
  width: 100%;
  padding: 1rem;

  background-color: var(--light);
  border: 1px solid var(--gray-300, #e5e7eb);
  border-radius: var(--border-radius);
  font-family: var(--font-family);
  font-size: 16px;
  color: var(--black);
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: var(--primary);
}

.form-input--error {
  border-color: var(--error);
}

.form-input--error:focus {
  border-color: var(--error);
}

.form-error {
  font-size: 14px;
  color: var(--error);
  margin-top: -0.25rem;
}
</style>
