// axios.js
import axios from 'axios';
import store from './store'; // Vuex 스토어를 임포트합니다. 경로는 실제 구조에 맞게 조정해야 합니다.

// axios 인스턴스를 생성합니다.
const api = axios.create({});

// 쿠키에서 값을 가져오는 함수
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

// 요청 인터셉터를 추가하여 모든 요청에 Authorization 헤더를 추가합니다.
api.interceptors.request.use(
    config => {
        // 로그인 또는 회원가입 요청의 경우 Authorization 헤더를 추가하지 않습니다.
        if (!config.url.includes('/members/signin') && !config.url.includes('/members/signup')) {
            const token = store.getters.getToken; // Vuex 스토어에서 토큰을 가져옵니다.
            if (token) {
                config.headers.Authorization = `Bearer ${token}`; // 토큰이 있다면 요청 헤더에 추가합니다.
            }
        }
        const XSRF = getCookie('XSRF-TOKEN');
        if (XSRF) {
            config.headers['X-XSRF-TOKEN'] = XSRF;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터 설정
axios.interceptors.response.use(response => {
    return response;
}, error => {
    if (error.response.status === 401) {
        // 로그인 페이지로 리다이렉트
        window.location = '/members/signin';
    }
    return Promise.reject(error);
});

export default api;
