<template>
  <QuestionInput @add="addQuestion" />
  <QuestionList :questions="questions" @delete="deleteQuestion" />
  <DeleteModal v-if="showDeleteModal" @confirm="confirmDelete" @cancel="cancelDelete" />
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { storeToRefs } from 'pinia';

import { useQuestionListStore } from "@/features";

import QuestionInput from "./component/QuestionInput.vue";
import QuestionList from "./component/QuestionList.vue";
import DeleteModal from "./component/DeleteModal.vue";

const route = useRoute();

const questionListStore = useQuestionListStore();

const { questions } = storeToRefs(questionListStore);
const showDeleteModal = ref(false);
const questionToDelete = ref(null);

onMounted(async () => {
  try {
    const teamId = route.params.teamId;
    const memberId = route.params.memberId;

    await questionListStore.fetchQuestionList(teamId, memberId);
  } catch (error) {
    console.log("err", error);
  }
});

const addQuestion = async (text) => {
  try {
    const teamId = route.params.teamId;

    await questionListStore.createQuestion(teamId, text)
  } catch (error) {
    console.log("err", error);
  }
};

const deleteQuestion = (id) => {
  questionToDelete.value = id;
  showDeleteModal.value = true;
};

const confirmDelete = () => {
  if (questionToDelete.value !== null) {
    questionListStore.deleteQuestion(questionToDelete.value);
  }
  showDeleteModal.value = false;
  questionToDelete.value = null;
};

const cancelDelete = () => {
  showDeleteModal.value = false;
  questionToDelete.value = null;
};
</script>

<style scoped></style>
