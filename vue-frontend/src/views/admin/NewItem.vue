<script setup>
import {onMounted, ref} from 'vue';
import AdminSideBar from "@/components/AdminSideBar.vue";
import axios from "@/axios.js";
import router from "@/router";


const categories = ref([]); // 셀렉트 박스에 표시될 옵션들을 저장할 반응형 변수
const brands = ref([]); // 셀렉트 박스에 표시될 옵션들을 저장할 반응형 변수
const categoriSelect = ref(''); // 선택된 옵션을 저장할 반응형 변수
const brandSelect = ref(''); // 선택된 옵션을 저장할 반응형 변수

const itemData = ref({ // 상품 정보를 저장할 반응형 변수
  itemNm: '',
  price: '',
  stockNumber: '',
  itemDetail: '',
  itemSellStatus: '',
  itemImgFile: [],
});
const handleFilesUpload = (event) => {
  {
    itemData.value.itemImgFile = event.target.files;
  }
}

// API로부터 브랜드 목록을 가져오는 함수
const getBrands = () => {
  axios.get('/brands')
      .then(response => {
        brands.value = response.data.map(item => ({
          value: item.id, // API 응답 구조에 따라 조정 필요
          text: item.name // API 응답 구조에 따라 조정 필요
        }));
        // 데이터 로딩 후 첫 번째 옵션의 value를 selectedOption에 할당
        if (brands.value.length > 0) {
          brandSelect.value = brands.value[0].value;
        }
      })
      .catch(error => {
        console.error('error', error);
      });
};
const getCategories = () => {
  axios.get('/categories')
      .then(response => {
        categories.value = response.data.map(item => ({
          value: item.id, // API 응답 구조에 따라 조정 필요
          text: item.cateName // API 응답 구조에 따라 조정 필요
        }));
        // 데이터 로딩 후 첫 번째 옵션의 value를 selectedOption에 할당
        if (categories.value.length > 0) {
          categoriSelect.value = categories.value[0].value;
        }
      })
      .catch(error => {
        console.error('error', error);
      });
};
// 서버로 상품 데이터를 보내는 함수
const submitData = () => {
  const formData = new FormData();
  // 상품 정보 필드를 formData에 추가
  formData.append('itemNm', itemData.value.itemNm);
  formData.append('price', itemData.value.price);
  formData.append('stock', itemData.value.stockNumber);
  formData.append('itemDetail', itemData.value.itemDetail);
  formData.append('itemSellStatus', itemData.value.itemSellStatus);
  formData.append('category', categoriSelect.value);
  formData.append('brand', brandSelect.value);

  axios.post('/admin/newItemAjax', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).then(response => {
    alert(response.data);
    router.push('/admin/AdminPage');
  }).catch(error => {
    alert(error);
  });
}
// 컴포넌트가 마운트되었을 때 getBrands 함수 호출
onMounted(getBrands);
onMounted(getCategories);
</script>

<template>
  <div class="content-wrapper">
    <!-- 사이드바 -->
    <AdminSideBar/>
    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div class="row gx-4 gx-lg-5 align-items-center">
        <form id="form" enctype="multipart/form-data" @submit.prevent="submitData">
          <!-- 오류 메시지를 표시할 컨테이너 -->
          <div id="formErrors" class="col-12 mt-3"></div>
          <div class="row g-3">
            <div class="col-12">
              <label class="form-label">상품 상태</label><br/>
              <input id="SELL" v-model="itemData.itemSellStatus" name="itemSellStatus" type="radio" value="SELL"><label
                for="SELL">판매중</label>
              <br/>
              <input id="SOLD_OUT" v-model="itemData.itemSellStatus" name="itemSellStatus" type="radio"
                     value="SOLD_OUT"><label for="SOLD_OUT">품절</label>
              <br/>
            </div>
            <div class="col-12">
              <label class="form-label" for="brand">카테고리</label>
              <select v-model="categoriSelect" class="form-select form-select-sm">
                <option v-for="option in categories" :key="option.value" :value="option.value">
                  {{ option.text }}
                </option>
              </select>
            </div>

            <div class="col-12">
              <label class="form-label" for="category">브랜드</label>
              <select v-model="brandSelect" class="form-select form-select-sm">
                <option v-for="option in brands" :key="option.value" :value="option.value">
                  {{ option.text }}
                </option>
              </select>
            </div>

            <div class="col-12">
              <label class="form-label" for="itemNm">상품명</label>
              <input id="itemNm" v-model="itemData.itemNm" class="form-control" name="itemNm" placeholder="상품명을 입력해주세요"
                     type="text">
            </div>

            <div class="col-12">
              <label class="form-label" for="itemDetail">상품 설명</label>
              <input id="itemDetail" v-model="itemData.itemDetail" class="form-control" name="itemDetail"
                     placeholder="상품 설명을 입력하세요." type="text">
            </div>

            <div class="col-12">
              <label class="form-label" for="price">상품 가격</label>
              <input id="price" v-model="itemData.price" class="form-control" name="price" placeholder="상품 가격을 입력하세요."
                     type="number">
            </div>

            <div class="col-12">
              <label class="form-label" for="stock">상품 재고</label>
              <input id="stock" v-model="itemData.stockNumber" class="form-control" name="stock"
                     placeholder="상품 재고를 입력하세요." type="number">
            </div>

            <div class="form-group">
              <div class="custom-file img-div">
                <input class="custom-file-input" multiple name="itemImgFile" type="file" @change="handleFilesUpload">
                <label class="custom-file-label">상품이미지 선택</label>
              </div>
            </div>
          </div>
          <hr class="my-4">
        </form>
        <div style="text-align: center">
          <button id="formBtn" class="w-100 btn btn-primary btn-lg mb-3" type="button" @click="submitData">상품 등록
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
