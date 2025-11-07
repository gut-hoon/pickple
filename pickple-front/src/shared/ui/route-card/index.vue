<template>
  <Card>
    <button
      class="route-button cursor-pointer !rounded-button whitespace-nowrap"
      @click="routeTo">
      <span>{{ label }}</span>
      <i class="fas fa-chevron-right"></i>
    </button>
  </Card>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { Card } from '@/shared/ui';

const props = defineProps({
  label: { type: String, required: true },
  to: { type: String, required: true },
});

const router = useRouter();

const routeTo = () =>  {
  if (props.to) {
    router.push(props.to);
  }
}
</script>

<style scoped>
.route-button {
  width: 100%;
  background: none;
  border: none;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: var(--black);
  font-size: 16px;
  border-radius: calc(var(--border-radius) - 4px);
  transition: background-color 0.2s;
}

.route-button:hover {
  background-color: var(--light);
}

.route-button i {
  color: var(--primary-dark);
  font-size: 16px;
}
</style>