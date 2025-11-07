  <template>
    <div class="error-container">

      <div class="error-content">
        <div class="error-code">500</div>
        <h1>서버 오류가 발생했습니다</h1>
        <p>
          일시적인 오류가 발생했습니다.<br />
          잠시 후 다시 시도해주세요.
        </p>

        <div class="button-group">
          <button class="primary-btn" @click="goHome">
            메인으로 돌아가기
          </button>
          <button class="secondary-btn" @click="goBack">
            이전 페이지로
          </button>
        </div>
      </div>
    </div>
  </template>

  <script setup>
  import { useRouter } from 'vue-router';

  const router = useRouter();

  const goHome = () => {
    router.push('/');
  };

  const goBack = () => {
    router.back();
  };
  </script>

  <style scoped>
  .error-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    padding: 20px;
    background-color: var(--white);
  }

  .logo {
    width: 120px;
    height: auto;
    margin-bottom: 60px;
    opacity: 0.5;
  }

  .error-content {
    text-align: center;
    max-width: 400px;
  }

  .error-code {
    font-size: 80px;
    font-weight: 700;
    color: #e0e0e0;
    margin-bottom: 20px;
    line-height: 1;
  }

  .error-content h1 {
    font-size: 24px;
    font-weight: 700;
    color: #555;
    margin-bottom: 16px;
  }

  .error-content p {
    font-size: 15px;
    color: #888;
    line-height: 1.6;
    margin-bottom: 40px;
  }

  .button-group {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .primary-btn,
  .secondary-btn {
    padding: 14px 28px;
    border: none;
    border-radius: 6px;
    font-size: 15px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s;
  }

  .primary-btn {
    background-color: var(--primary);
    color: white;
  }

  .primary-btn:hover {
    opacity: 0.9;
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .secondary-btn {
    background-color: var(--white);
    color: #555;
    border: 1px solid #e0e0e0;
  }

  .secondary-btn:hover {
    background-color: #f8f8f8;
    border-color: #d0d0d0;
  }

  .primary-btn:active,
  .secondary-btn:active {
    transform: translateY(0);
  }
  </style>
