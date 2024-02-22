<script setup>
import {computed} from 'vue';
import {useStore} from 'vuex';
import {useRouter} from 'vue-router';

const store = useStore();
const router = useRouter();

// userInfo와 token을 계산된 속성으로 만들어 사용
const userInfo = computed(() => store.state.userInfo);
const token = computed(() => store.state.userToken);

// 로그아웃 액션
function logOut() {
  store.dispatch('logout');
  router.push('/main');
}
</script>

<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container px-4 px-lg-5">
        <a class="navbar-brand" href="@{/main}">쇼핑몰</a>
        <button aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"
                class="navbar-toggler" data-bs-target="#navbarSupportedContent" data-bs-toggle="collapse" type="button">
          <span class="navbar-toggler-icon"></span></button>
        <div id="navbarSupportedContent" class="collapse navbar-collapse">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
            <li class="nav-item"><a aria-current="page" class="nav-link" href="@{/main}">Home</a></li>
            <li v-if="userInfo.role === 'ROLE_ADMIN'" class="nav-item"><a class="nav-link" href="@{admin/newitem}">upload</a>
            </li>
            <li v-if="userInfo.role === 'ROLE_ADMIN'" class="nav-item"><a class="nav-link"
                                                                          href="@{admin/manage}">Manage</a>
            </li>
            <li class="nav-item dropdown">
              <a id="navbarDropdown" aria-expanded="false" class="nav-link dropdown-toggle" data-bs-toggle="dropdown"
                 href="#" role="button">Shop</a>
              <ul aria-labelledby="navbarDropdown" class="dropdown-menu">
                <li><a class="dropdown-item" href="@{/item/list}">All Products</a></li>
                <li>
                  <hr class="dropdown-divider"/>
                </li>
                <li><a class="dropdown-item" href="#!">Popular Items</a></li>
                <li><a class="dropdown-item" href="#!">New Arrivals</a></li>
              </ul>
            </li>
            <form class="d-md-inline-flex">
              <input aria-label="Search" class="form-control me-2" placeholder="Search" type="search">
              <button class="btn btn-outline-success" type="submit">Search</button>
            </form>

          </ul>
          <form v-if="!token">
            <router-link class="btn btn-outline-dark" to="/members/signin">
              로그인
            </router-link>
          </form>
          <form v-if="!token">
            <router-link class="btn btn-outline-dark" to="/members/signup">
              회원가입
            </router-link>
          </form>

          <form v-if="token">
            <div class="dropdown">
              <button id="dropdownMenuButton" aria-expanded="false"
                      aria-haspopup="true" class="btn btn-default dropdown-toggle btn-outline-dark"
                      data-bs-toggle="dropdown" type="button">
                <span id="mberName">{{ userInfo.username }}{{ userInfo.role === 'ROLE_ADMIN' ? ' 판매자님' : '' }}</span>
              </button>
              <ul id="mberNemu" aria-labelledby="dropdownMenuButton" class="dropdown-menu">
                <li v-if="userInfo.role === 'ROLE_ADMIN'">
                  <router-link class="dropdown-item" to="admin/AdminPage">판매자페이지</router-link>
                </li>
                <li v-if="userInfo.role === 'ROLE_ADMIN'">
                  <router-link class="dropdown-item" to="admin/ItemManage">상품관리</router-link>
                </li>
                <li v-if="userInfo.role === 'ROLE_ADMIN'">
                  <router-link class="dropdown-item" to="admin/SaleList">판매내역</router-link>
                </li>
                <li v-if="userInfo.role === 'ROLE_USER'"><a class="dropdown-item" href="/user/myPage">마이페이지</a></li>
                <li v-if="userInfo.role === 'ROLE_USER'"><a class="dropdown-item" href="/user/cart">장바구니</a></li>
                <li v-if="userInfo.role === 'ROLE_USER'"><a class="dropdown-item" href="/user/orderHist">주문내역</a></li>
              </ul>
            </div>
          </form>

          <form v-if="token">
            <button class="btn btn-outline-dark" type="submit" @click="logOut();">
              로그아웃
            </button>
          </form>

        </div>
      </div>
    </nav>

  </div>
</template>

<style scoped>

</style>
