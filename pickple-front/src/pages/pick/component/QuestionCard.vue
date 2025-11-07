<template>
  <Card class="question-card">
    <div class="card-header">
      <i class="fas fa-question-circle icon-blue"></i>
      <h2 class="question-title">질문</h2>
    </div>
    <p class="question-text">{{ question? question.content:null }}</p>
  </Card>
</template>

<script setup>
import { Card } from '@/shared/ui'
defineProps({
  question: Object
})
</script>

<style scoped>
.question-card {
  padding: 1.3rem;
  margin-bottom: 1rem;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.75rem;
}

.question-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #111827;
}

.question-text {
  font-size: 1.5rem;
  font-weight: 700;
  color: #111827;
  line-height: 1.5;
  text-align: center;
  margin-top: 0.75rem;
}

.icon-blue {
  color: #3b82f6;
  font-size: 1.25rem;
}
</style>
