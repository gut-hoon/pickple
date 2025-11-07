import { app } from '@/app';
import { setRouter } from '@/entities/axiosInstance';
import { router } from '@/app/providers/router';

setRouter(router);

app.mount('#app');
