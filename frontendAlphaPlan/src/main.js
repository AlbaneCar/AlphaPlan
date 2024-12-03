import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
// import router from './Router/base.js'
import router from '../src/router/base'
import PrimeVue from 'primevue/config';
import 'primevue/resources/primevue.min.css'
import 'primevue/resources/themes/aura-light-green/theme.css'
import axios from 'axios'
import Toast from 'vue-toastification';
import 'vue-toastification/dist/index.css';
import Tooltip from 'primevue/tooltip';


const app = createApp(App);
app.use(PrimeVue);
app.use(router, PrimeVue);
app.use(Toast, {
    transition: "Vue-Toastification__bounce",
    maxToasts: 5,
    newestOnTop: true,
    timeout: 3000,
    position : "top-center",
  });
app.directive('tooltip', Tooltip);
app.config.globalProperties.$http = axios; // Ajout d'Axios comme propriété globale
app.mount('#app')