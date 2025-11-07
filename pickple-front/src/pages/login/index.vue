<template>
  <div class="login-container">
    <img src="/Logo.png" alt="PICKPLE" class="logo" />
    <div class="welcome-text">
      <h1>픽플에 오신 것을 환영합니다</h1>
      <h1>구글 계정으로 시작하세요</h1>
    </div>
    <button class="google-login-btn" @click="handleGoogleLogin">
      <img
        src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg"
        alt="Google"
        class="google-icon"
      />
      Google로 시작하기
    </button>
  </div>
</template>

 <script setup>
  const apiBaseUrl = import.meta.env.VITE_API_URL;

  const handleGoogleLogin = () => {
      window.location.href = `${apiBaseUrl}/oauth2/authorization/google`;
  };
  </script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  padding: 20px;
  background-color: var(--white);
}

.logo {
  width: 180px;
  height: auto;
  margin-bottom: 80px;
  opacity: 0.7;
}

.welcome-text {
  text-align: center;
  margin-bottom: 50px;
}

.welcome-text h1 {
  font-size: 22px;
  font-weight: 700;
  color: #555;
  margin: 0 0 8px 0;
}

.welcome-text h1:last-child {
  margin-bottom: 0;
}

.google-login-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 12px 28px;
  background-color: var(--white);
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 15px;
  font-weight: 500;
  color: #333;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: all 0.2s;
}

.google-login-btn:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
  border-color: #d0d0d0;
}

.google-login-btn:active {
  transform: scale(0.98);
}

.google-icon {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}
</style>
