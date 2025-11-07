<template>
  <div class="create-team-page">
    <div class="profile-block">
      <div class="image-section">
        <div class="background-image-container">
          <img
            :src="backgroundImageUrl"
            alt="Team Background Image Preview"
            class="background-image"
          />
          <label for="team-bg-image" class="edit-background-button">
            <i class="fas fa-camera"></i>
          </label>
          <input
            id="team-bg-image"
            type="file"
            @change="onBgFileChange"
            accept="image/*"
            class="file-input"
          />
        </div>

        <div class="avatar-container">
          <img :src="imageUrl" alt="Team Image Preview" class="avatar-image" />
          <label for="team-image" class="edit-avatar-button">
            <i class="fas fa-camera"></i>
          </label>
          <input
            id="team-image"
            type="file"
            @change="onFileChange"
            accept="image/*"
            class="file-input"
          />
        </div>
      </div>
    </div>

    <div class="edit-block">
      <div class="form-section">
        <Form
          label="팀 이름"
          v-model="teamName"
          placeholder="팀 이름을 입력하세요"
          :error="teamNameError"
        ></Form>
      </div>

      <div class="save-section">
        <EventButton label="팀 생성하기" @click="createTeam" :disabled="!isFormValid" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { createTeam as createTeamApi } from '@/entities/team';
import { EventButton, Form } from '@/shared/ui';
import defaultBgImage from '/ProfileBackground.png';
import defaultAvatarImage from '/ProfileTeamAvatar.png';
import { useCurrentTeamStore } from '@/features';

const router = useRouter();
const teamName = ref('');
const teamImage = ref(null);
const teamBackgroundImage = ref(null);
const imageUrl = ref(defaultAvatarImage);
const backgroundImageUrl = ref(defaultBgImage);
const teamNameError = ref('');
const currentTeamStore = useCurrentTeamStore();

const onFileChange = e => {
  const file = e.target.files?.[0];
  if (!file) return;
  teamImage.value = file;
  if (imageUrl.value.startsWith('blob:')) {
    URL.revokeObjectURL(imageUrl.value);
  }
  imageUrl.value = URL.createObjectURL(file);
};

const onBgFileChange = e => {
  const file = e.target.files?.[0];
  if (!file) return;
  teamBackgroundImage.value = file;
  if (backgroundImageUrl.value.startsWith('blob:')) {
    URL.revokeObjectURL(backgroundImageUrl.value);
  }
  backgroundImageUrl.value = URL.createObjectURL(file);
};

onBeforeUnmount(() => {
  if (imageUrl.value.startsWith('blob:')) {
    URL.revokeObjectURL(imageUrl.value);
  }
  if (backgroundImageUrl.value.startsWith('blob:')) {
    URL.revokeObjectURL(backgroundImageUrl.value);
  }
});

const validateTeamName = () => {
  if (!teamName.value.trim()) {
    teamNameError.value = '팀 이름을 입력해주세요.';
    return false;
  }else if(teamName.value.length > 10) {
    teamNameError.value = '팀 이름은 최대 10자까지 가능합니다.';
    return false;
  }
  teamNameError.value = '';
  return true;
};

const isFormValid = computed(() => {
  return !teamNameError.value && teamName.value.trim() !== '';
});

watch(teamName, validateTeamName);

const createTeam = async () => {
  if (!validateTeamName()) {
    return;
  }

  const formData = new FormData();
  formData.append('teamName', teamName.value);

  if (teamImage.value) {
    formData.append('avatarImage', teamImage.value);
  }

  if (teamBackgroundImage.value) {
    formData.append('backgroundImage', teamBackgroundImage.value);
  }

  try {
    const response = await createTeamApi(formData);
    console.log(response, '팀 생성 응답');
    currentTeamStore.enterTeam({
      id: response.teamId,
      name: teamName.value,
      avatarImage: response.avatarImageUrl,
      backgroundImage: response.backgroundImageUrl,
    });
    currentTeamStore.setParticipant({id:response.participantId});
    
    alert('팀이 성공적으로 생성되었습니다.');
    router.replace(`/team/${response.teamId}/${response.participantId}`);
  } catch (error) {
    alert('팀 생성에 실패했습니다.');
    console.error(error);
  }
};
</script>

<style scoped>
.create-team-page {
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
