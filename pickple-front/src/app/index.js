import {createApp} from 'vue'
import {router,pinia} from './providers'
import App from './App.vue'

import './styles/index.css'
import '@fortawesome/fontawesome-free/css/all.min.css'

export const app = createApp(App)
    .use(pinia)
    .use(router)