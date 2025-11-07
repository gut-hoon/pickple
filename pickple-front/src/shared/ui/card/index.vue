<template>
  <div class="card">
    <slot></slot>
  </div>
</template>

<script setup>
</script>

<style scoped>
.card {
  background-color: var(--white);
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  border: 1px solid #F3F4F6;
}
</style>