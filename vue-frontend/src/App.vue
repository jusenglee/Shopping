// src/App.vue
<template>
  <div id="app">
    <AppHeader v-if="!$route.meta.hideHeade"/>
    <router-view/>
    <AppFooter v-if="!$route.meta.hideFooter"/>
  </div>
</template>

<script>
import AppHeader from './components/AppHeader.vue';
import AppFooter from './components/AppFooter.vue';

export default {
  components: {
    AppHeader,
    AppFooter,
  }
};
</script>
<style>
</style>
