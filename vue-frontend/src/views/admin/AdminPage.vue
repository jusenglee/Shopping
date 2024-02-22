<script setup>
import AdminSideBar from "@/components/AdminSideBar.vue";
</script>

<template>
  <!-- 사이드바와 메인 콘텐츠를 감싸는 컨테이너 -->
  <div class="content-wrapper">
    <!-- 사이드바 -->
    <AdminSideBar/>

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div class="row">
        <div class="col-6">
          <div class="card">
            <div class="card-text">
              <h4>상품을 등록하고 판매를 시작하세요.</h4>
              <p>수많은 고객들이 판매자님의 상품을 기다리고 있습니다!</p>
              <p>지금 바로 상품을 등록하고 판매를 시작해보세요.</p>
            </div>
            <div class="row" style="width: 100%">
              <div class="col">
                <button class="btn btn-primary" type="button">
                  <router-link to="/admin/NewItem">상품 등록 바로가기</router-link>
                </button>
              </div>
              <div class="col" style="padding: 10px;">
                <div class="illustration">
                  <!-- 이미지 삽입, 실제 이미지 경로로 대체 필요 -->
                  <img alt="Illustration" src="@/assets/images/num1.png">
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-6">
          <div class="card">
            <div class="card-text">
              <h4>판매 중인 상품의 정보가 잘못되셨나요?</h4>
              <p>현재 판매중인 상품의 정보는 언제든지 수정 가능합니다.</p>
              <p>아래를 눌러 상품 관리로 이동해주세요</p>
            </div>
            <div class="row" style="width: 100%">
              <div class="col">
                <button class="btn btn-primary" type="button">
                  <router-link to="/admin/ItemManage">상품 관리 바로가기</router-link>
                </button>
              </div>
              <div class="col" style="padding: 10px;"><!-- 해당 col이 남은 여백을 모두 차지해야함-->
                <div class="illustration">
                  <img alt="Illustration" src="@/assets/images/num2.png"><!-- 해당 이미지는 오른쪽 아래에 위치-->
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-6">
          <div class="card">
            <h4>사업자 정보를 확인해주세요.</h4>
            <p>상품 판매를 진행 전 판매자님의 정보를 확인해주세요.</p>
            <button class="btn btn-primary" type="button">
              <router-link to="/admin/AdminModify">개인정보 수정</router-link>
            </button>
          </div>
        </div>
        <div class="col-6">
          <div class="card">
            <h4>현재 판매 현황을 확인 해보세요.</h4>
            <p>현재 판매중인 상품에서 어떤 상품이 제일 잘 팔릴지 확인 해보세요!</p>
            <button class="btn btn-primary" type="button">
              <router-link to="/admin/SaleList">판매 내역</router-link>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
