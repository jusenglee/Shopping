import {createApp} from 'vue';
import router from './router';
import App from "@/App.vue";
import store from './store';
import './assets/css/styles.css';
import './assets/css/mainCss.css';
import './assets/css/switch.css';
import './assets/css/button.css';
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-icons/font/bootstrap-icons.css'
import VueCookies from 'vue-cookies';
import dateFormatter from './plugins/dateFormatter';

// Import FilePond styles
import "filepond/dist/filepond.min.css";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.css";

const app = createApp(App);
// Vue 3 방식으로 전역 메소드 추가
app.config.globalProperties.$getCookie = function (name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}
// 앱 인스턴스를 생성하고 라우터를 사용하도록 설정합니다.
app.use(dateFormatter).use(router).use(VueCookies).use(store).mount('#app');

