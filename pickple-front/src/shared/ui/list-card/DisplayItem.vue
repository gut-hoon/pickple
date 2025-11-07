<template>
  <div class="item">
    <span class="label">
      <span v-if="leftItem" class="label-icon">{{ leftItem }}</span>
      <span class="label-text">{{ label }}</span>
    </span>
    <span v-if="rightItem" class="right-item">{{ rightItem }}</span>
  </div>
</template>

<script setup>
const props = defineProps({
  label: { type: String, required: true },
  leftItem: { type: Object, default: null },
  rightItem: { type: Object, default: null },
});
</script>

<style scoped>
.item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid var(--light);
}

.item:last-child {
  border-bottom: none;
}

.label {
  display: flex;
  align-items: center;
  gap: 8px;
}

.label-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;

  min-width: 28px;
  min-height: 28px;

  /* 문자열일 경우 적용 */
  font-size: 14px;
  font-weight: 600;
  color: var(--white);
  background-color: var(--primary);

  overflow: hidden;
}

.label-text {
  flex: 1;
  color: var(--black);
  font-size: 16px;
}

.right-item {
  font-size: 14px;
  color: var(--primary-dark);
  font-weight: 500;
}
</style>