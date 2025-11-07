<template>
  <div class="app-layout">
    <AppBar/>
    <div class="main-content">
      <RouterView />
    </div>
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router'
import { AppBar } from '@/widgets/index.js';
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
  --medium-gray: #999999;
  --background: #F8F9FA;

  --border-radius: 0.75rem;
  --shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  --font-family: 'Noto Sans KR', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
}

html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  overflow: hidden;
}

.app-layout {
  max-width: 480px;
  height: 100vh;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;

  background-color: var(--light);

  overflow-y: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
  
  font-family: var(--font-family);
}

.app-layout::-webkit-scrollbar {
  display: none;
}

.main-content {
  height: fit-content;
  width: 100%;
  padding: 16px;
  box-sizing: border-box;

  overflow: visible;
}
</style>