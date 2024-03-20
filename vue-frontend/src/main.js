import {createApp} from 'vue';
import router from './router';
import App from "@/App.vue";
import store from './store';
import './assets/css/styles.css';
import './assets/css/mainCss.css';
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-icons/font/bootstrap-icons.css'
import VueCookies from 'vue-cookies';
import dateFormatter from './plugins/dateFormatter';


// 앱 인스턴스를 생성하고 라우터를 사용하도록 설정합니다.
createApp(App).use(dateFormatter).use(router).use(VueCookies).use(store).mount('#app');
