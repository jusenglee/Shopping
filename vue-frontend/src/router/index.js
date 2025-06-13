import {createRouter, createWebHistory} from 'vue-router';

// 라우터 설정을 정의합니다.
const routes = [{
    path: '/', name: 'Home', component: () => import('@/views/MainPage.vue') // Lazy load 방식
}, {
    path: '/main', name: 'Home2', component: () => import('@/views/MainPage.vue') // Lazy load 방식
}, {
    path: '/members/signin', name: 'signin', component: () => import('@/views/LoginPage.vue'), // Lazy load 방식
}, {
    path: '/members/signup', name: 'signup', component: () => import('@/views/LoginPage.vue'), // Lazy load 방식
},

// 판매자 마이페이지
    {
    path: '/seller/sellerMyPage', component: () => import('@/views/seller/SellerMyPage.vue'), name: 'SellerMyPage'
}, {path: '/seller/sellerNewItemPage', component: () => import('@/views/seller/SellerNewItemPage.vue'), name: 'SellerNewItemPage'}, {
    path: '/seller/sellerModifyItemPage/:id', component: () => import('@/views/seller/SellerNewItemPage.vue'), name: 'SellerModifyItemPage'
}, {
    path: '/seller/sellerItemManagePage', component: () => import('@/views/seller/SellerItemManagePage.vue'), name: 'SellerItemManagePage'
}, {
    path: '/seller/sellerModifyInfo', component: () => import('@/views/seller/SellerModifyInfo.vue'), name: 'SellerModifyInfo'
}, {
    path: '/seller/saleList', component: () => import('@/views/seller/SaleList.vue'), name: 'SaleList'
},
];

// 라우터 인스턴스를 생성합니다.
const router = createRouter({
    history: createWebHistory(), routes
});

export default router;
