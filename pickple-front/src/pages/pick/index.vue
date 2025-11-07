<template>
  <QuestionCard 
    v-if="question" 
    :question="question" 
  />

  <MemberSelectCard
    v-if="question"
    :participants="participants"
    :selectedParticipant="selectedParticipant"
    @select="selectMember"
  />

  <FooterButtons
    v-if="question"
    :hasSelection="!!selectedParticipant"
    @next="nextQuestion"
    @skip="skipQuestion"
  />

  <NoQuestionCard 
    v-else 
    @refresh="handleRefresh"
    @goToTeamMain="router.push(`/team/${teamId}/${participantId}`)"
  />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';

import { usePickStore, useTeamParticipantStore, useQuestionListStore } from '@/features';

import QuestionCard from './component/QuestionCard.vue';
import MemberSelectCard from './component/MemberSelectCard.vue';
import FooterButtons from './component/FooterButtons.vue';
import NoQuestionCard from './component/NoQuestionCard.vue';

const route = useRoute();
const router = useRouter();
const pickStore = usePickStore();
const teamParticipantStore = useTeamParticipantStore();
const questionListStore = useQuestionListStore();

const { question } = storeToRefs(pickStore);
const { participants } = storeToRefs(teamParticipantStore);
const selectedParticipant = ref(null)

const teamId = route.params.teamId;

onMounted(async () => {
  try {
    pickStore.fetchQuestion(teamId);
    teamParticipantStore.fetchTeamParticipants(teamId);
  } catch (error) {
    console.log("err", error);
  }
});

const selectMember = (participantId) => {
  selectedParticipant.value = participantId;
}

const nextQuestion = () => {
  if (!selectedParticipant.value) return;
  
  try {
    pickStore.pick(teamId, question.value.id, selectedParticipant.value);
    selectedParticipant.value = null
  } catch (error) {
    console.log("err", error);
  }
}

const skipQuestion = () => {
  try {
    pickStore.skip(teamId, question.value.id);
    selectedParticipant.value = null
  } catch (error) {
    console.log("err", error);
  }
}

const handleRefresh = async () => {
  try {
    await questionListStore.refreshAllDoneQuestion(teamId);
    await pickStore.fetchQuestion(teamId);
  } catch (error) {
    console.log('err', error);
  }
};
</script>
