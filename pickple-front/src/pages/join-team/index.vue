<template>
  <div class="join-team-page">
    <div class="profile-block">
      <div class="image-section">
        <div class="background-image-container">
          <img
            :src="backgroundPreview || defaultBgImage"
            alt="Background Preview"
            class="background-image"
          />
          <label for="bg-image-input" class="edit-background-button">
            <i class="fas fa-camera"></i>
          </label>
          <input
            id="bg-image-input"
            type="file"
            @change="handleBackgroundChange"
            accept="image/*"
            class="file-input"
          />
        </div>

        <div class="avatar-container">
          <img :src="avatarPreview || defaultAvatarImage" alt="Avatar Preview" class="avatar-image" />
          <label for="avatar-image-input" class="edit-avatar-button">
            <i class="fas fa-camera"></i>
          </label>
          <input
            id="avatar-image-input"
            type="file"
            @change="handleAvatarChange"
            accept="image/*"
            class="file-input"
          />
        </div>
      </div>
    </div>

    <div class="edit-block">
      <div class="form-section">
        <Form
          label="팀 코드"
          v-model="form.teamCode"
          placeholder="초대받은 코드를 입력하세요"
          :error="errors.teamCode"
        />
        <Form
          label="닉네임"
          v-model="form.nickname"
          placeholder="팀에서 사용할 닉네임을 입력하세요"
          :error="errors.nickname"
        />
      </div>

      <div class="save-section">
        <EventButton label="팀 합류하기" @click="handleSubmit" :disabled="!isFormValid" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onBeforeUnmount, computed } from 'vue';
import { useRouter } from 'vue-router';
import { Form, EventButton } from '@/shared/ui';
import { joinTeam } from '@/entities/team';
import defaultBgImage from '/ProfileBackground.png';
import defaultAvatarImage from '/ProfileMemberAvatar.png';

const router = useRouter();

const form = reactive({
  teamCode: '',
  nickname: '',
  avatarImage: null,
  backgroundImage: null,
});

const errors = reactive({
  teamCode: '',
  nickname: '',
});

const avatarPreview = ref(null);
const backgroundPreview = ref(null);

const handleAvatarChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    form.avatarImage = file;
    if (avatarPreview.value && avatarPreview.value.startsWith('blob:')) {
      URL.revokeObjectURL(avatarPreview.value);
    }
    avatarPreview.value = URL.createObjectURL(file);
  }
};

const handleBackgroundChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    form.backgroundImage = file;
    if (backgroundPreview.value && backgroundPreview.value.startsWith('blob:')) {
      URL.revokeObjectURL(backgroundPreview.value);
    }
    backgroundPreview.value = URL.createObjectURL(file);
  }
};

onBeforeUnmount(() => {
  if (avatarPreview.value && avatarPreview.value.startsWith('blob:')) {
    URL.revokeObjectURL(avatarPreview.value);
  }
  if (backgroundPreview.value && backgroundPreview.value.startsWith('blob:')) {
    URL.revokeObjectURL(backgroundPreview.value);
  }
});

const isFormValid = computed(() => {
  return form.teamCode.trim() !== '' && form.nickname.trim() !== '';
});

const validateForm = () => {
  let isValid = true;
  if (!form.teamCode.trim()) {
    errors.teamCode = '팀 코드를 입력해주세요.';
    isValid = false;
  } else {
    errors.teamCode = '';
  }

  if (!form.nickname.trim()) {
    errors.nickname = '닉네임을 입력해주세요.';
    isValid = false;
  } else {
    errors.nickname = '';
  }
  return isValid;
};

const handleSubmit = async () => {
  if (!validateForm()) {
    return;
  }

  const formData = new FormData();
  formData.append('invitationCode', form.teamCode);
  formData.append('nickname', form.nickname);
  if (form.avatarImage) {
    formData.append('avatarImage', form.avatarImage);
  }
  if (form.backgroundImage) {
    formData.append('backgroundImage', form.backgroundImage);
  }

  try {
    await joinTeam(formData);
    alert('팀에 성공적으로 합류했습니다!');
    router.push('/home');
  } catch (error) {
    console.error('팀 합류 실패:', error);
    alert('팀 합류에 실패했습니다. 입력 정보를 확인해주세요.');
  }
};
</script>

<style scoped>
.join-team-page {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.profile-block {
  width: 100%;
  margin: 0 auto;
}

.image-section {
  position: relative;
  padding-bottom: 60px;
}

.background-image-container {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
  background-color: var(--gray-200, #f0f0f0);
}

.background-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.edit-background-button {
  position: absolute;
  top: 1rem;
  right: 1rem;
  width: 40px;
  height: 40px;
  background-color: rgba(0, 0, 0, 0.5);
  border: none;
  border-radius: 50%;
  color: var(--white);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.edit-background-button:hover {
  background-color: rgba(0, 0, 0, 0.7);
}

.avatar-container {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
}

.avatar-image {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 4px solid var(--white);
  object-fit: cover;
  background-color: var(--gray-200, #f0f0f0);
}

.edit-avatar-button {
  position: absolute;
  bottom: 8px;
  right: 8px;
  width: 32px;
  height: 32px;
  background-color: var(--primary);
  border: 2px solid var(--white);
  border-radius: 50%;
  color: var(--white);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.edit-avatar-button:hover {
  background-color: var(--primary-dark);
}

.file-input {
  display: none;
}

.edit-block {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  padding: 0 1.5rem;
}

.save-section {
  padding: 0 1.5rem 2rem;
}
</style>