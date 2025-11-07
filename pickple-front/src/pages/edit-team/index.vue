<template>
  <!-- 로딩 중 -->
  <div v-if="isLoading" class="loading-container">
    <LoadingSpinner />
  </div>

  <!-- 프로필 및 편집 섹션 -->
  <div v-else class="edit-team-page">
    <!-- 프로필 이미지 섹션 -->
    <div class="profile-block">
      <div class="image-section">
        <div class="background-image-container">
          <img :src="teamInfo.backgroundImage" alt="배경 사진" class="background-image" />
          <button
            class="edit-background-button"
            @click="editBackgroundImage"
          >
            <i class="fas fa-camera"></i>
          </button>
        </div>

        <div class="avatar-container">
          <img :src="teamInfo.avatarImage" alt="프로필 사진" class="avatar-image" />
          <button class="edit-avatar-button" @click="editAvatarImage">
            <i class="fas fa-camera"></i>
          </button>
        </div>
      </div>
    </div>

    <div class="edit-block">
      <!-- 팀 정보 입력 폼 -->
      <div class="form-section">
        <Form
          label="팀 이름"
          v-model="teamInfo.name"
          placeholder="팀 이름을 입력하세요"
          :error="errors.name"
        />
      </div>

      <!-- 변경 저장 버튼 -->
      <div class="save-section">
        <EventButton
          label="저장하기"
          :disabled="!canSave"
          @click="saveChanges"
        />
      </div>
    </div>

    <!-- 이미지 선택 모달 -->
    <ImageSelectModal
      :isOpen="imageModal.isOpen"
      :title="imageModal.title"
      :showDefault="imageModal.showDefault"
      @close="closeImageModal"
      @upload="handleModalUpload"
      @default="handleModalDefault"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from "vue";
import { useRoute, useRouter } from 'vue-router';
import { useTeamProfileStore } from '@/features'
import { EventButton, LoadingSpinner, Form, ImageSelectModal } from '@/shared/ui'
import { openImageUploader } from '@/shared/lib'

const route = useRoute();
const teamProfileStore = useTeamProfileStore();
const router = useRouter();
const participantId = route.params.participantId;

const isLoading = ref(true);

// 편집 중인 팀 정보
const teamInfo = reactive({
  name: '',
  avatarImage: '',
  backgroundImage: '',
});

// 입력폼별 오류 메시지
const errors = reactive({
  name: '',
});

const DEFAULT_AVATAR_IMAGE = '/ProfileTeamAvatar.png';
const DEFAULT_BACKGROUND_IMAGE = '/ProfileBackground.png';

/**
 * 유효성 검사
 */

// 필드 별 유효성 검사
const validateField = (field) => {
  switch (field) {
    case 'name':
      errors.name = (!teamInfo.name || String(teamInfo.name).trim() === '')
        ? '팀 이름을 입력해주세요'
        : '';
      teamInfo.name.length > 10 ? errors.name = '팀 이름은 최대 10자까지 가능합니다.' : '';
      return !errors.name;
  }
};

// 모든 폼이 유효한지 검사
const isFormValid = () => {
  return validateField('name');
};

// 실시간 필드 검사
watch(() => teamInfo.name, () => validateField('name'));

// 기존값에서 변경이 있는지 검사
const hasChanges = computed(() => {
  return teamInfo.name !== (teamProfileStore.name || '') ||
    teamInfo.avatarImage !== (teamProfileStore.avatarImage || '') ||
    teamInfo.backgroundImage !== (teamProfileStore.backgroundImage || '');
});

// 변경 있음 && 오류 없음 -> 저장 버튼 활성화
const canSave = computed(() => {
  return hasChanges.value && isFormValid();
});


/**
 * 이미지 수정
 */

// 이미지 모달 상태
const imageModal = reactive({
  isOpen: false,
  title: '',
  type: '',
  showDefault: true,
});

// 모달 열기
const openImageModal = (type) => {
  imageModal.type = type;
  imageModal.title = type === 'avatar' ? '프로필 사진 선택' : '배경 사진 선택';

  const currentImage = type === 'avatar' ? teamInfo.avatarImage : teamInfo.backgroundImage;
  const defaultImage = type === 'avatar' ? DEFAULT_AVATAR_IMAGE : DEFAULT_BACKGROUND_IMAGE;
  imageModal.showDefault = currentImage !== defaultImage;

  imageModal.isOpen = true;
};

// 모달 닫기
const closeImageModal = () => {
  imageModal.isOpen = false;
  imageModal.type = '';
  imageModal.title = '';
  imageModal.showDefault = true;
};

// 모달에서 업로드 선택
const handleModalUpload = () => {
  const currentType = imageModal.type;

  openImageUploader({
    onSuccess: (result) => {
      if (currentType === 'avatar') {
        teamInfo.avatarImage = result;
      } else if (currentType === 'background') {
        teamInfo.backgroundImage = result;
      }
    },
    onError: (error) => {
      console.error('이미지 업로드 중 오류:', error);
      alert(error.message || '이미지 처리 중 오류가 발생했습니다.');
    }
  });
};

// 모달에서 기본 이미지 선택
const handleModalDefault = () => {
  const currentType = imageModal.type;

  if (currentType === 'avatar') {
    teamInfo.avatarImage = DEFAULT_AVATAR_IMAGE;
  } else if (currentType === 'background') {
    teamInfo.backgroundImage = DEFAULT_BACKGROUND_IMAGE;
  }
};

// 카메라 버튼 클릭 시 모달 열기
const editBackgroundImage = () => {
  openImageModal('background');
};

const editAvatarImage = () => {
  openImageModal('avatar');
};

const saveChanges = async () => {
  if (!isFormValid()) {
    alert('입력 정보를 확인해주세요.');
    return;
  }

  try {
    const editedInformation = {
      ...teamInfo,
      teamId: route.params.teamId,
      removeAvatarImage: teamInfo.avatarImage === DEFAULT_AVATAR_IMAGE,
      removeBackgroundImage: teamInfo.backgroundImage === DEFAULT_BACKGROUND_IMAGE,
    }
    await teamProfileStore.tryEditTeam(editedInformation);
    alert('팀 정보가 저장되었습니다.');
    router.replace(`/team/${editedInformation.teamId}/${participantId}`);
  } catch (error) {
    console.error('팀 정보 저장 중 오류:', error);
    alert('저장에 실패했습니다. 다시 시도해주세요.');
  }
};

/* ========================================
 * 라이프사이클
 * ======================================== */

onMounted(async () => {
  try {
    const teamId = route.params.teamId;
    await teamProfileStore.fetchTeamProfile(teamId);

    teamInfo.name = teamProfileStore.teamName || '';
    teamInfo.avatarImage = teamProfileStore.teamAvatarImage || DEFAULT_AVATAR_IMAGE;
    teamInfo.backgroundImage = teamProfileStore.teamBackgroundImage || DEFAULT_BACKGROUND_IMAGE;

  } finally {
    isLoading.value = false;
  }
});
</script>

<style scoped>
/* 로딩 컨테이너 */
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.edit-team-page {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

/* 프로필 섹션 */
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
  transition: background-color 0.2s;
}

.edit-avatar-button:hover {
  background-color: var(--primary-dark);
}

.file-input {
  display: none;
}

/* 이메일 표시 */
.email-section {
  text-align: center;
  font-size: 14px;
  color: var(--gray-600, #666);
}

/* 페이지 컨테이너 */
.edit-block {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
}

/* 정보 폼 */
.form-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* 저장 버튼 */
.save-section {
  padding: 0 1.5rem 2rem;
}
</style>
