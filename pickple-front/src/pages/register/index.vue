<template>
  <div class="register-container">
    <img src="/Logo.png" alt="PICKPLE" class="logo" />

    <div class="register-content">
      <div class="title-section">
        <h1>추가 정보 입력</h1>
        <p>서비스 이용을 위해 아래 정보를 입력해주세요</p>
      </div>

      <div class="form-section">
        <Form
          label="이름"
          v-model="fullName"
          type="text"
          placeholder="이름을 입력하세요"
          :error="errors.fullName"
        />

        <Form
          label="생년월일"
          v-model="birthDate"
          type="date"
          :error="errors.birthDate"
        />
      </div>

      <EventButton
        label="완료"
        :disabled="!isFormValid || isSubmitting"
        @click="handleSubmit"
      />

      <p v-if="isSubmitting" class="loading-message">처리 중...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { Form, EventButton } from '@/shared/ui';
import { useMeStore } from '@/features';

const router = useRouter();
const meStore = useMeStore();

// 폼 데이터
const fullName = ref('');
const birthDate = ref('');
const isSubmitting = ref(false);

// 에러 상태
const errors = ref({
  fullName: '',
  birthDate: '',
});

// 폼 유효성 검사
const isFormValid = computed(() => {
  return fullName.value.trim() !== '' && birthDate.value !== '';
});

// 유효성 검사 함수
const validateForm = () => {
  let isValid = true;

  // 이름 검증
  if (!fullName.value.trim()) {
    errors.value.fullName = '이름을 입력해주세요';
    isValid = false;
  } else if (fullName.value.trim().length < 2) {
    errors.value.fullName = '이름은 2자 이상이어야 합니다';
    isValid = false;
  } else {
    errors.value.fullName = '';
  }

  // 생년월일 검증
  if (!birthDate.value) {
    errors.value.birthDate = '생년월일을 선택해주세요';
    isValid = false;
  } else {
    const selectedDate = new Date(birthDate.value);
    const today = new Date();

    if (selectedDate > today) {
      errors.value.birthDate = '유효한 생년월일을 입력해주세요';
      isValid = false;
    } else {
      errors.value.birthDate = '';
    }
  }

  return isValid;
};

// 제출 핸들러
const handleSubmit = async () => {
  if (!validateForm()) {
    return;
  }

  isSubmitting.value = true;
  try {
    // meStore의 register action 호출
    await meStore.register(fullName.value, birthDate.value);

    // 성공 시 메인 페이지로 이동
    router.push('/');
  } catch (error) {
    // 401, 403, 404, 500은 인터셉터가 자동 처리
    // 400만 여기서 처리
    const status = error.response?.status;
    const message = error.response?.data?.message;

    if (status === 400) {
      if (message?.includes('이미')) {
        alert('이미 등록이 완료되었습니다.');
        router.push('/');
      } else if (error.response?.data?.fieldErrors) {
        // 필드별 에러 표시
        errors.value.fullName = error.response.data.fieldErrors.fullName || '';
        errors.value.birthDate = error.response.data.fieldErrors.birthDate || '';
      } else {
        alert(message || '입력 정보를 확인해주세요.');
      }
    }
    // 나머지 에러는 인터셉터가 처리
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
.register-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 20px;
  background-color: var(--white);
}

.logo {
  width: 180px;
  height: auto;
  margin-bottom: 60px;
  opacity: 0.7;
}

.register-content {
  width: 100%;
  max-width: 360px;
}

.title-section {
  text-align: center;
  margin-bottom: 40px;
}

.title-section h1 {
  font-size: 22px;
  font-weight: 700;
  color: #555;
  margin: 0 0 12px 0;
}

.title-section p {
  font-size: 14px;
  font-weight: 400;
  color: #888;
  margin: 0;
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 32px;
}

.loading-message {
  text-align: center;
  font-size: 14px;
  color: var(--primary-dark);
  margin-top: 16px;
}
</style>
