<template>
  <!-- Header-->
  <header class="bg-dark py-5">
    <SliderComponent/>
  </header>
  <!-- Section-->
  <section class="py-5">
    <div class="container px-4 px-lg-5 mt-5">
      <div class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
      </div>
    </div>
  </section>
</template>

<script>
import SliderComponent from '@/components/SliderComponent.vue';

export default {
  components: {
    SliderComponent
  }
};
</script>

<style scoped>
.main-page {
  text-align: center;
}
</style>
