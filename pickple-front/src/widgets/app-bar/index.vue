<template>
  <header class="header">
    <button v-if="!isMain" class="back-button" @click="goBack" aria-label="뒤로가기">&lt;</button>

    <img
      src="/Logo.png"
      alt="로고"
      class="logo"
      role="button"
      tabindex="0"
      @click="goMain"
    />

    <div class="interaction-section">
      <img 
        v-if="showAvatar"
        :src="meStore.avatarImage"
        alt="아바타"
        class="avatar"
      />
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router'
import { useMeStore } from '@/features/index.js';

const route = useRoute();
const router = useRouter();
const meStore = useMeStore();

const isMain = computed(() => route.name === 'main');

const hiddenAvatarPaths = ['/login'];
const showAvatar = computed(() => {
  if (hiddenAvatarPaths.includes(route.path)) return false;

  if (route.path.startsWith('/member-profile')) {
    const memberId = String(route.params.memberId ?? '');
    const myId = String(meStore.getMyId?.() ?? meStore.getMyId ?? '');
    return memberId !== myId;
  }

  return true;
});

const goBack = () => router.back();

const goMain = () => { if(!isMain.value) router.push('/'); }

</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  width: 100%;
  padding: 16px;
  padding-left: 16px;
  padding-right: 16px;
  box-sizing: border-box;

  background-color: var(--white);
  border-bottom: 1px solid #E5E7EB;
  
  min-height: 56px;
}

.logo {
  height: 28px;
  cursor: pointer;
}

.back-button {
  background: transparent;
  border: none;
  font-size: 20px;
  line-height: 1;
  cursor: pointer;
  padding: 4px 8px;
}

.interaction-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header {
  position: relative;
}
.header .logo {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}
.header .back-button {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
}

.header .interaction-section {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

/* 반응형 */
@media (max-width: 320px) {
  .header {
    padding-left: 12px;
    padding-right: 12px;
  }
}
</style>