<script setup>

</script>

<template>
  <div class="sidebar">
    <ul>
      <li :class="{ active: $route.name === 'AdminPage' }">
        <router-link to="/admin/AdminPage">대시보드</router-link>
      </li>
      <li :class="{ active: $route.name === 'NewItem' }">
        <router-link to="/admin/NewItem">상품 등록</router-link>
      </li>
      <li :class="{ active: $route.name === 'ItemManage' }">
        <router-link to="/admin/ItemManage">상품 관리</router-link>
      </li>
      <li :class="{ active: $route.name === 'AdminModify' }">
        <router-link to="/admin/AdminModify">개인정보 수정</router-link>
      </li>
      <li :class="{ active: $route.name === 'SaleList' }">
        <router-link to="/admin/SaleList">판매 내역</router-link>
      </li>
      <!-- Add other sidebar items here -->
    </ul>
  </div>
</template>

<style scoped>

</style>
