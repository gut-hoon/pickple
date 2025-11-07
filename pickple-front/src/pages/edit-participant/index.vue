<template>
  <!-- 로딩 중 -->
  <div v-if="isLoading" class="loading-container">
    <LoadingSpinner />
  </div>

  <!-- 프로필 및 편집 섹션 -->
  <div v-else class="edit-participant-page">
    <!-- 프로필 이미지 섹션 -->
    <div class="profile-block">
      <div class="image-section">
        <div class="background-image-container">
          <img :src="participantInfo.backgroundImage" alt="배경 사진" class="background-image" />
          <button
            class="edit-background-button"
            @click="editBackgroundImage"
          >
            <i class="fas fa-camera"></i>
          </button>
        </div>

        <div class="avatar-container">
          <img :src="participantInfo.avatarImage" alt="프로필 사진" class="avatar-image" />
          <button class="edit-avatar-button" @click="editAvatarImage">
            <i class="fas fa-camera"></i>
          </button>
        </div>
      </div>
    </div>

    <div class="edit-block">
      <!-- 팀 이름 표시 -->
      <div class="team-name-section">
        <h2 class="team-name">팀 · {{ participantStore.teamName }}</h2>
      </div>

      <!-- 사용자 정보 입력 폼 -->
      <div class="form-section">
        <Form
          label="닉네임"
          v-model="participantInfo.nickname"
          placeholder="닉네임을 입력하세요"
          :error="errors.nickname"
        />
      </div>

      <!-- 상호작용 섹션 -->
      <div class="board-section">
        <!-- 팀 나가기-->
        <div class="user-management">
          <button class="delete-link" @click="deleteAccount">
            팀 나가기
          </button>
        </div>
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
import { useRoute } from 'vue-router';
import { useParticipantStore } from '@/features'
import { EventButton, LoadingSpinner, Form, ImageSelectModal } from '@/shared/ui'
import { openImageUploader } from '@/shared/lib'

// TODO: 캘린더 커스텀

const route = useRoute();
const participantStore = useParticipantStore();

const isLoading = ref(true);

// 편집 중인 참가자 정보
const participantInfo = reactive({
  nickname: '',
  avatarImage: '',
  backgroundImage: '',
});

// 입력폼별 오류 메시지
const errors = reactive({
  nickname: '',
});


/**
 * 유효성 검사
 * - 입력폼 별 유효성 검사
 * - 저장 가능 여부 검사
 */
const NICKNAME_FIELD = 'nickname';

// 필드 별 유효성 검사
const validateField = (field) => {
  switch (field) {
    case NICKNAME_FIELD:
      errors.nickname = (!participantInfo.nickname || String(participantInfo.nickname).trim() === '')
        ? '닉네임을 입력해주세요'
        : '';
      return !errors.nickname;
  }
};

// 모든 폼이 유효한지 검사
const isFormValid = () => {
  return validateField(NICKNAME_FIELD);
};

// 실시간 필드 검사
watch(() => participantInfo.nickname, () => validateField(NICKNAME_FIELD));

// 기존값에서 변경이 있는지 검사
const hasChanges = computed(() => {
  return participantInfo.nickname !== (participantStore.nickname || '') ||
    participantInfo.avatarImage !== (participantStore.avatarImage || '') ||
    participantInfo.backgroundImage !== (participantStore.backgroundImage || '');
});

// 변경 있음 && 오류 없음 -> 저장 버튼 활성화
const canSave = computed(() => {
  return hasChanges.value && isFormValid();
});


/**
 * 이미지 수정
 */
const imageModal = reactive({
  isOpen: false,
  title: '',
  type: '',
  showDefault: true,
});

const DEFAULT_AVATAR_IMAGE = '/ProfileMemberAvatar.png';
const DEFAULT_BACKGROUND_IMAGE = '/ProfileBackground.png';

// 모달 열기
const openImageModal = (type) => {
  imageModal.type = type;
  imageModal.title = type === 'avatar' ? '프로필 사진 선택' : '배경 사진 선택';

  // 현재 이미지가 기본 이미지인지 확인
  const currentImage = type === 'avatar' ? participantInfo.avatarImage : participantInfo.backgroundImage;
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
        participantInfo.avatarImage = result;
      } else if (currentType === 'background') {
        participantInfo.backgroundImage = result;
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
    participantInfo.avatarImage = DEFAULT_AVATAR_IMAGE;
    console.log('아바타 이미지를 기본으로 변경');
  } else if (currentType === 'background') {
    participantInfo.backgroundImage = DEFAULT_BACKGROUND_IMAGE;
    console.log('배경 이미지를 기본으로 변경');
  }
};

// 카메라 버튼 클릭 시 모달 열기
const editBackgroundImage = () => {
  openImageModal('background');
};

const editAvatarImage = () => {
  openImageModal('avatar');
};


/**
 * 액션 핸들러
 */

const saveChanges = async () => {
  try {
    const editedInformation = {
      ... participantInfo,
      removeAvatarImage: participantInfo.avatarImage == DEFAULT_AVATAR_IMAGE,
      removeBackgroundImage: participantInfo.backgroundImage == DEFAULT_BACKGROUND_IMAGE,
    }

    await participantStore.tryEditParticipant(editedInformation);
    alert('저장되었습니다.');
  } catch (error) {
    console.error('사용자 정보 저장 중 오류:', error);
    alert('저장에 실패했습니다. 다시 시도해주세요.');
  }
};

const deleteAccount = () => {
  if (confirm("정말로 나가겠습니까?")) {
    console.log("팀 나가기");
  }
};

/* ========================================
 * 라이프사이클
 * ======================================== */

onMounted(async () => {
  try {
    await participantStore.fetchParticipant(route.params.participantId);

    // 폼에 초기값 설정
    participantInfo.nickname = participantStore.nickname || '';
    participantInfo.avatarImage = participantStore.avatarImage || '';
    participantInfo.backgroundImage = participantStore.backgroundImage || '';
  } finally {
    isLoading.value = false;
  }
})
</script>

<style scoped>
/* 로딩 컨테이너 */
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.edit-participant-page {
  width: 100%;
  display: flex;
  flex-direction: column;
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

/* 팀 이름 섹션 */
.edit-block {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  width: 100%;
  margin: 0 auto;
}

.team-name-section {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.team-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--gray-900, #1a1a1a);
  margin: 0;
  text-align: center;
}

/* 정보 폼 */
.form-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* 보드 섹션 */
.board-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;

  padding-left: 0.75rem;
  padding-right: 0.5rem;
  box-sizing: border-box;
}

/* 사용자 관리 섹션 (로그아웃 & 회원 탈퇴) */
.user-management {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1rem 0;
}

.logout-link {
  background: none;
  padding: 0;
  border: none;
  color: var(--gray-600, #666);
  font-size: 16px;
  text-decoration: underline;
  text-align: left;
  cursor: pointer;
}

.logout-link:hover {
  color: var(--gray-800, #333);
}

.delete-link {
  background: none;
  padding: 0;
  border: none;
  color: var(--danger, #ff4444);
  font-size: 16px;
  text-decoration: underline;
  text-align: left;
  cursor: pointer;
}

.delete-link:hover {
  color: var(--danger-dark, #cc0000);
}

/* 저장 버튼 */
.save-section {
  padding: 0 1.5rem 2rem;
}
</style>
