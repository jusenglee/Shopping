<script setup>

</script>

<template>
  <div class="sidebar">
    <ul>
      <li :class="{ active: $route.name === 'SellerMyPage' }">
        <router-link to="/seller/sellerMyPage">
          대시보드
        </router-link>
      </li>
      <li :class="{ active: $route.name === 'SellerNewItemPage' }">
        <router-link to="/seller/sellerNewItemPage">
          상품 등록
        </router-link>
      </li>
      <li :class="{ active: $route.name === 'SellerItemManagePage' }">
        <router-link to="/seller/sellerItemManagePage">
          상품 관리
        </router-link>
      </li>
      <li :class="{ active: $route.name === 'SellerModifyInfo' }">
        <router-link to="/seller/sellerModifyInfo">
          개인정보 수정
        </router-link>
      </li>
      <li :class="{ active: $route.name === 'SaleList' }">
        <router-link to="/seller/saleList">
          판매 내역
        </router-link>
      </li>
      <!-- Add other sidebar items here -->
    </ul>
  </div>
</template>

<style scoped>

</style>
