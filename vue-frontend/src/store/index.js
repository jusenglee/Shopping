// src/store/index.js
import {createStore} from 'vuex';
import createPersistedState from 'vuex-persistedstate';

export default createStore({
    state() {
        return {
            userToken: null,
            userInfo: {},
        };
    },
    mutations: {
        setUserToken(state, token) {
            state.userToken = token;
        },
        setUserInfo(state, userInfo) {
            state.userInfo = userInfo;
        },
    },
    getters: {
        getToken: state => state.userToken,
    },
    actions: {
        login({commit}, {token, userInfo}) {
            commit('setUserToken', token);
            commit('setUserInfo', userInfo);
            // 로컬 스토리지에 토큰 저장
        },
        logout({commit}) {
            commit('setUserToken', null);
            commit('setUserInfo', {});
            // 로컬 스토리지에서 토큰 제거
            localStorage.removeItem('user-token');
            localStorage.removeItem('userInfo');
        },
    },
    plugins: [createPersistedState({
        paths: ['userToken', 'userInfo'], // 영속화할 상태 지정
    })],
});
