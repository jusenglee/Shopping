<script setup>
import AdminSideBar from "@/components/AdminSideBar.vue";
</script>

<template>
  <!-- 사이드바와 메인 콘텐츠를 감싸는 컨테이너 -->
  <div class="content-wrapper">
    <!-- 사이드바 -->
    <AdminSideBar />

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div class="card-box">
        <div class="card">
          <div class="card-text">
            <h4>상품을 등록하고 판매를 시작하세요.</h4>
            <p>수많은 고객들이 판매자님의 상품을 기다리고 있습니다!</p>
            <p>지금 바로 상품을 등록하고 판매를 시작해보세요.</p>
          </div>
          <div
            class="row"
            style="width: 100%"
          >
            <div class="col">
              <router-link
                class="button button--saqui button--round-l button--text-thick"
                to="/admin/NewItem"
                type="button"
              >
                상품 등록하러 가기
              </router-link>
            </div>
            <div class="col">
              <div class="illustration">
                <!-- 이미지 삽입, 실제 이미지 경로로 대체 필요 -->
                <img
                  alt="Illustration"
                  src="@/assets/images/003.png"
                >
              </div>
            </div>
          </div>
        </div>
        <div class="card">
          <div class="card-text">
            <h4>판매 중인 상품의 정보가 잘못되셨나요?</h4>
            <p>현재 판매중인 상품의 정보는 언제든지 수정 가능합니다.</p>
            <p>아래를 눌러 상품 관리로 이동해주세요</p>
          </div>
          <div
            class="row"
            style="width: 100%"
          >
            <div class="col">
              <router-link
                class="button button--saqui button--round-l button--text-thick"
                to="/admin/ItemManage"
                type="button"
              >
                상품 목록으로 가기
              </router-link>
            </div>
            <div class="col">
              <div class="illustration">
                <img
                  alt="Illustration"
                  src="@/assets/images/002.png"
                >
              </div>
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-text">
            <h4>잠깐!</h4>
            <p>판매 시작 전, 사업자 정보를 확인하셨나요?</p>
            <p>아래를 눌러 판매자 정보를 수정해주세요</p>
          </div>
          <div
            class="row"
            style="width: 100%"
          >
            <div class="col">
              <router-link
                class="button button--saqui button--round-l button--text-thick"
                to="/admin/AdminModify"
                type="button"
              >
                판매 정보로 가기
              </router-link>
            </div>
            <div class="col">
              <div class="illustration">
                <img
                  alt="Illustration"
                  src="@/assets/images/001.png"
                >
              </div>
            </div>
          </div>
        </div>
        <div class="card">
          <div class="card-text">
            <h4>현재 판매 현황을 확인 해보세요.</h4>
            <p>현재 판매중인 상품에서 어떤 상품이 제일 잘 팔릴지 확인 해보세요!</p>
          </div>
          <div
            class="row"
            style="width: 100%"
          >
            <div class="col">
              <router-link
                class="button button--saqui button--round-l button--text-thick"
                to="/admin/ItemManage"
                type="button"
              >
                판매 현황으로 가기
              </router-link>
            </div>
            <div class="col">
              <div class="illustration">
                <div class="illustration">
                  <img
                    alt="Illustration"
                    src="@/assets/images/004.png"
                  >
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped>

</style>
