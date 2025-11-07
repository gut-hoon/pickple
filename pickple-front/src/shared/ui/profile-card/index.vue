<template>
  <Card>
    <div class="profile-card">
      <div class="profile-background">
        <img 
          :src="backgroundImage"
          alt="프로필 배경"
          class="background-image"
        />
        <button
          v-if="toEdit"
          class="edit-button"
          @click="toEditPage">
          <i class="fas fa-edit"></i>
        </button>
      </div>
      
      <div class="profile-info">
        <div class="avatar">
          <img 
            :src="avatarImage"
            alt="프로필 아바타"
            class="avatar-image"
          />
        </div>

        <div>
          <h2 class="name">{{ name }}</h2>
          <h2 v-if="description" class="description">{{ description }}</h2>
        </div>
      </div>
    </div>
  </Card>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { Card } from '@/shared/ui';

const props = defineProps({
  avatarImage: { type: String, required: true, },
  backgroundImage: { type: String, required: true },
  name: { type: String, required: true },
  description: { type: String, default: null },
  toEdit: { type: String, default: null },
});

const router = useRouter();

const toEditPage = () => {
  if (props.toEdit) {
    router.push(props.toEdit);
  }
}
</script>


<style scoped>
.profile-card {
  overflow: hidden;
}

.profile-background {
  position: relative;
  height: 120px;
  background: linear-gradient(135deg, var(--primary-light) 0%, var(--primary) 100%);
  overflow: hidden;
}

.background-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.edit-button {
  position: absolute;
  top: 12px;
  right: 12px;
  background-color: var(--white);
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-dark);
  box-shadow: var(--shadow);
  transition: transform 0.2s;
  font-size: 14px;
}

.edit-button:hover {
  transform: scale(1.05);
}

.profile-info {
  display: flex;
  align-items: flex-end;
  padding: 16px;
  gap: 12px;
  margin-top: -30px;
  position: relative;
  z-index: 1;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 40px;
  background-color: var(--white);
  box-shadow: var(--shadow);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  overflow: hidden;
  position: relative;
  z-index: 2;
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.name {
  font-size: 20px;
  font-weight: 600;
  color: var(--black);
  margin: 0;
}

.description {
  font-size: 14px;
  font-weight: 400;
  color: var(--primary-dark);
  margin: 4px 0 0 0;
  line-height: 1.4;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px;
}
</style>