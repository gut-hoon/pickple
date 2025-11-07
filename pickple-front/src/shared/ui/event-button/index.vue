<template>
  <button
    class="event-button"
    :disabled="disabled"
    @click="onClick"
  >
    {{ label }}
  </button>
</template>

<script setup>
defineProps({
  label: {
    type: String,
    required: true
  },
  disabled: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['click']);

const onClick = () => {
  emit('click');
};
</script>

<style scoped>

.event-button {
  width: 100%;
  padding: 1rem;

  border: none;
  border-radius: var(--border-radius);
  background-color: var(--primary);
  color: var(--dark);
  font-size: 16px;
  font-weight: 600;
  transition: background-color 0.2s;
}

.event-button:hover:not(:disabled) {
  color: var(--white);
  background-color: var(--primary-dark, #a0c9d9);
}

.event-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>