// axios.js
import axios from 'axios';
import store from './store'; // Vuex 스토어를 임포트합니다. 경로는 실제 구조에 맞게 조정해야 합니다.

// axios 인스턴스를 생성합니다.
const api = axios.create({});

// 요청 인터셉터를 추가하여 모든 요청에 Authorization 헤더를 추가합니다.
api.interceptors.request.use(
    config => {
        const token = store.getters.getToken; // Vuex 스토어에서 토큰을 가져옵니다.
        if (token) {
            config.headers.Authorization = `Bearer ${token}`; // 토큰이 있다면 요청 헤더에 추가합니다.
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default api;
