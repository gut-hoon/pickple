import { useMeStore } from '@/features/member/meStore';

export const routes = [
  {
    path: '/',
    alias: '/home',
    name: 'main',
    component: () => import('./main/index.vue'),
  },

  {
    path: '/member-profile/:memberId',
    name: 'memberProfile',
    component: () => import('./member-profile/index.vue'),
  },

  {
    path: '/receive-pick',
    name: 'receivePick',
    component: () => import('./receive-pick/index.vue'),
  },

  {
    path: '/login',
    name: 'login',
    component: () => import('./login/index.vue'),
  },

  {
    path: '/edit-member',
    name: 'editMember',
    component: () => import('./edit-member/index.vue'),
  },

  {
    path: '/participant-profile/:participantId',
    name: 'participantProfile',
    component: () => import('./participant-profile/index.vue'),
  },

  {
    path: '/edit-participant/:participantId',
    name: 'editParticipant',
    component: () => import('./edit-participant/index.vue'),
  },

  {
    path: '/team/:teamId/:participantId',
    name: 'team',
    component: () => import('./team/index.vue'),
  },

  {
    path: '/question-management/:teamId/:memberId',
    name: 'questionManagement',
    component: () => import('./question-management/index.vue'),
  },

  {
    path: '/register',
    name: 'register',
    component: () => import('./register/index.vue'),
  },

  {
    path: '/pick/:teamId/:participantId',
    name: 'pick',
    component: () => import('./pick/index.vue'),
  },

  {
    path: '/create-team',
    name: 'createTeam',
    component: () => import('./create-team/index.vue'),
  },

  {
    path: '/team/:teamId/invite-code',
    name: 'inviteCode',
    component: () => import('./invite-code/index.vue'),
  },

  {
    path: '/join-team',
    name: 'joinTeam',
    component: () => import('./join-team/index.vue'),
  },

  {
    path: '/error/404',
    name: 'error404',
    component: () => import('./error/404.vue'),
  },

  {
    path: '/error/500',
    name: 'error500',
    component: () => import('./error/500.vue'),
  },

  {
    path: '/:pathMatch(.*)*',
    name: 'notFound',
    redirect: '/error/404',
  },

  {
    path: '/edit-team/:teamId/:participantId',
    name: 'editTeam',
    component: () => import('./edit-team/index.vue'),
  },
];
