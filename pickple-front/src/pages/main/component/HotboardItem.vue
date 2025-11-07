<template>
  <div class="hotboard-item">
    <div class="header">
      <span class="icon">{{ icon }}</span>
      <h3 class="label">{{ label }}</h3>
    </div>
    <div v-if="item" class="content">
      <img v-if="item.avatarImage" :src="item.avatarImage" alt="Avatar" class="avatar"/>
      <div class="info">
        <p class="name">{{ itemLabel }}</p>
        <p class="count">{{ item.count }}번의 픽</p>
      </div>
    </div>
    <div v-else class="empty-content">
      <p>정보가 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  icon: { type: String, required: true },
  label: { type: String, required: true },
  item: { type: Object, default: null },
  itemLabel: { type: String, default: '' },
});
</script>

<style scoped>
.hotboard-item {
  padding: 16px 0;
  border-bottom: 1px solid #eee;
}
.hotboard-item:last-child {
  border-bottom: none;
}

.header {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 12px;
}

.icon {
  font-size: 20px;
}

.label {
  font-size: 18px;
  font-weight: 600;
  color: #050505;
}

.content {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-left: 32px;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.info {
  display: flex;
  flex-direction: column;
}

.name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin: 0;
}

.count {
  font-size: 14px;
  color: #6B7280;
  margin: 0;
}

.empty-content {
  padding-left: 32px;
  color: #9CA3AF;
  font-size: 14px;
}
</style>