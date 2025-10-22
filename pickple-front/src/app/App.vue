<template>
  <RouterView />
</template>

<script setup>
import { RouterView } from 'vue-router'
</script>

<style>
:root {
  --primary: #B7DCEB;
  --primary-light: #DAEFF8;
  --primary-dark: #5E727B;

  --error: #FF3B30;
  --error-light: #E4606D;
  --error-dark: #E80F15;

  --white: #FFFFFF;
  --light: #F8F9FA;
  --black: #050505;
  --gray: #F3F4F6;

  --border-radius: 0.75rem;
  --shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
  --font-family: 'Noto Sans KR', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
}
</style>