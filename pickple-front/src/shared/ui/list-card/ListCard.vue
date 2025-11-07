<template>
  <Card>
    <div class="list-card">
      <h3 class="title">
        <i :class="titleIconClass"></i>
        {{ titleLabel }}
      </h3>

      <slot></slot>
    </div>
  </Card>
</template>

<script setup>
import { Card } from '@/shared/ui';

const props = defineProps({
  titleLabel: { type: String, default: null },
  titleIconClass: { type: String, default: null },
});

</script>

<style scoped>
.list-card {
  padding: 20px;
}

.title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: var(--black);
  margin: 0 0 16px 0;
}

.title i {
  color: var(--primary-dark);
  font-size: 18px;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
</style>