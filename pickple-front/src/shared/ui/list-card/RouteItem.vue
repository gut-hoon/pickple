<template>
  <div
    class="item" 
    @click="routeTo">
    <span class="label">
      <span v-if="leftItem" class="label-icon">{{ leftItem }}</span>
      <span class="label-text">{{ label }}</span>
    </span>
    <i class="fas fa-chevron-right" v-if="showNavigationIcon"></i>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const props = defineProps({
  label: { type: String, required: true },
  leftItem: { type: Object, default: null },
  showNavigationIcon: { type: Boolean, default: true },
  to: { type: String, required: true }
});

const router = useRouter();

const routeTo = () =>  {
  if (props.to) {
    router.push(props.to);
  }
}
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

.item:hover {
  background-color: var(--gray);
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
</style>