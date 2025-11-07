<template>
  <div class="team-item" @click="routeTo">
    <div class="info">
      <img :src="team.avatarImage" alt="Team Avatar" class="avatar"/>
      <span class="name">{{ team.name }}</span>
    </div>
    <i class="fas fa-chevron-right"></i>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useCurrentTeamStore } from '@/features';

const props = defineProps({
  team: { type: Object, required: true },
});

const router = useRouter();
const currentTeamStore = useCurrentTeamStore();

const routeTo = () =>  {
  if (props.team.id) {
    if (!currentTeamStore.participant) currentTeamStore.participant = {};
    currentTeamStore.participant.id = props.team.participantId;

    currentTeamStore.enterTeam(props.team);

    router.push(`/team/${props.team.id}/${props.team.participantId}`);
  }
}
</script>

<style scoped>
.team-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.team-item:last-child {
  border-bottom: none;
}

.team-item:hover {
  background-color: #f9f9f9;
}

.info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.fa-chevron-right {
  color: #6B7280;
}
</style>
