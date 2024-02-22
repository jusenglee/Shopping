<template>
  <header class="bg-dark py-5">
    <div class="container px-4 px-lg-5 my-5">
      <swiper :autoplay="true" :loop="true" :slides-per-view="1" :space-between="50">
        <swiper-slide><img alt="이미지 1" src="@/assets/images/Banner1.jpg"/></swiper-slide>
        <swiper-slide><img alt="이미지 2" src="@/assets/images/Banner2.jpg"/></swiper-slide>
        <swiper-slide><img alt="이미지 3" src="@/assets/images/Banner3.jpg"/></swiper-slide>
        <swiper-slide><img alt="이미지 4" src="@/assets/images/Banner4.jpg"/></swiper-slide>
        <!-- 추가 슬라이드 -->
      </swiper>
      swiperOption: {
      slidesPerView: 1,
      spaceBetween: 30,
      loop: true,
      pagination: {
      el: '.swiper-pagination',
      clickable: true
      },
      navigation: {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev'
      }
      },
    </div>
  </header>
</template>

<script>
// Swiper 6와 Vue 3용
import {Swiper, SwiperSlide} from 'swiper/vue';
import 'swiper/swiper-bundle.css';

export default {
  components: {
    Swiper,
    SwiperSlide
  }
};

</script>

<style scoped>
img {
  width: 100%;
  height: auto;
}

</style>
