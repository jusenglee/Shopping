import {createRouter, createWebHistory} from 'vue-router';

// 라우터 설정을 정의합니다.
const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/MainPage.vue') // Lazy load 방식
    },
    {
        path: '/main',
        name: 'Home2',
        component: () => import('@/views/MainPage.vue') // Lazy load 방식
    },
    {
        path: '/members/signin',
        name: 'signin',
        component: () => import('@/views/LoginPage.vue'), // Lazy load 방식
        meta: {hideFooter: true}
    },
    {
        path: '/members/signup',
        name: 'signup',
        component: () => import('@/views/LoginPage.vue'), // Lazy load 방식
        meta: {hideFooter: true}
    },
    // Admin 사이드바
    {path: '/admin/AdminPage', component: () => import('@/views/admin/AdminPage.vue'), name: 'AdminPage'},
    {path: '/admin/NewItem', component: () => import('@/views/admin/NewItem.vue'), name: 'NewItem'},
    {path: '/admin/ItemManage', component: () => import('@/views/admin/ItemManage.vue'), name: 'ItemManage'},
    {path: '/admin/AdminModify', component: () => import('@/views/admin/AdminModify.vue'), name: 'AdminModify'},
    {path: '/admin/SaleList', component: () => import('@/views/admin/SaleList.vue'), name: 'SaleList'},
    // 다른 라우트 설정들...
];

// 라우터 인스턴스를 생성합니다.
const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
