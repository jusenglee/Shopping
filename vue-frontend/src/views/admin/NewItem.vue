<script setup>
import {onMounted, ref} from 'vue';
import AdminSideBar from "@/components/AdminSideBar.vue";
import axios from "@/axios.js";
import router from "@/router";
import {useRoute} from 'vue-router';

let url = "/admin/newItem" //상품 등록, 수정에 따른 url 변경을 위한 변수

const itemData = ref({
  itemId: '',
  itemNm: '',
  price: '',
  stockNumber: '',
  itemDetail: '',
  category: 1,
  brand: 1,
  itemSellStatus: 'SELL',
  itemImgFile: [], // 사용자가 선택한 새 파일
  itemImgDtoList: [], // 이미 저장된 이미지 정보
  deleteImgIds: []
});
const brandList = ref([]);
const categoriesList = ref([]); // 카테고리 목록을 저장할 반응형 참조

const newImagePreviews = ref([]); //이미지 미리보기 제공을 위한 URL 리스트

const handleFilesUpload = (event) => {
  itemData.value.itemImgFile = Array.from(event.target.files);
  newImagePreviews.value = itemData.value.itemImgFile.map(file => URL.createObjectURL(file));
};
const removeSavedImage = (imageId) => {
  itemData.value.deleteImgIds.push(imageId);
  itemData.value.itemImgDtoList = itemData.value.itemImgDtoList.filter(image => image.id !== imageId);
};

const removeNewImage = (index) => {
  // newImagePreviews에서 URL을 제거
  newImagePreviews.value.splice(index, 1);
  // 실제 파일 목록에서도 제거
  itemData.value.itemImgFile.splice(index, 1);
};


// API로부터 브랜드 목록을 가져오는 함수
const getBrandAndCategories = () => {
  axios.get('/admin/brands')
      .then(response => {
        brandList.value = response.data.map(item => ({
          value: item.id, // API 응답 구조에 따라 조정 필요
          text: item.name // API 응답 구조에 따라 조정 필요
        }));
        console.log('brandList', brandList);
      })
      .catch(error => {
        console.error('error', error);
      });
  axios.get('/admin/categories')
      .then(response => {
        categoriesList.value = response.data.map(item => ({
          value: item.id, // API 응답 구조에 따라 조정 필요
          text: item.cateName // API 응답 구조에 따라 조정 필요
        }));
        console.log('categoriesList', categoriesList);
      })
      .catch(error => {
        console.error('error', error);
      });
};

// 서버로 상품 데이터를 보내는 함수
const submitData = () => {
  const formData = new FormData();
  // 상품 정보 필드를 formData에 추가
  formData.append('id', itemData.value.id);
  formData.append('itemNm', itemData.value.itemNm);
  formData.append('price', itemData.value.price);
  formData.append('stockNumber', itemData.value.stockNumber);
  formData.append('itemDetail', itemData.value.itemDetail);
  formData.append('itemSellStatus', itemData.value.itemSellStatus);
  formData.append('category', itemData.value.category);
  formData.append('brand', itemData.value.brand);
  itemData.value.itemImgFile.forEach(file => {
    formData.append('itemImgFile', file);
  });
  // 삭제할 이미지 ID를 formData에 추가
  itemData.value.deleteImgIds.forEach(id => {
    formData.append('deleteImgIds', id);
  });
  axios.post(url, formData, {
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

onMounted(getBrandAndCategories);
//상품 수정 URL을 통해 접근시
const route = useRoute(); // vue-router 4.x에서 제공하는 useRoute 컴포지션 API 사용
onMounted(() => {
  const productId = route.params.id; // params에서 직접 접근
  const expectedPath = `/admin/Modified/${productId}`;

  if (route.path === expectedPath) {
    axios.get(`/item/${productId}`)
        .then(response => {
          itemData.value = response.data.itemData;
          if (response.data.itemData.itemImgDtoList && response.data.itemData.itemImgDtoList.length > 0) {
            newImagePreviews.value = response.data.itemData.itemImgDtoList.map(imgDto => {
              return require(`@/assets/images/${imgDto.imgName}`);
            });
          }
        }).catch(error => console.error("Fetching item error: ", error));
    url = '/admin/modifyItem/'
  }
});
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
              <select v-model="itemData.category" class="form-select form-select-sm">
                <option v-for="option in categoriesList" :key="option.value" :value="option.value">
                  {{ option.text }}
                </option>
              </select>
            </div>

            <div class="col-12">
              <label class="form-label" for="category">브랜드</label>
              <select v-model="itemData.brand" class="form-select form-select-sm">
                <option v-for="option in brandList" :key="option.value" :value="option.value">
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

            <div class="col-12">
              <label class="form-label" for="image" style="margin-top: 10px">
                상품이미지 추가</label>
              <input id="image" class="form-control" multiple name="itemImgFile" type="file"
                     @change="handleFilesUpload">
            </div>

            <!-- 기존 이미지 미리보기 -->
            <div v-if="itemData.itemImgDtoList && itemData.itemImgDtoList.length > 0" class="image-preview-container">
              <div v-for="(image) in itemData.itemImgDtoList" :key="`saved-${image.id}`" class="image-preview">
                <img :alt="image.oriImgName" :src="require(`@/assets/images/${image.imgName}`)"
                     class="preview-image">
                <!-- 이미지 삭제 버튼 -->
                <button @click="removeSavedImage(image.id)">삭제</button>
              </div>
            </div>

            <!-- 새 이미지 미리보기 -->
            <div v-if="newImagePreviews && newImagePreviews.length > 0" class="new-image-preview-container">
              <div v-for="(imageSrc, index) in newImagePreviews" :key="`new-${index}`" class="image-preview">
                <img :src="imageSrc" class="preview-image">
                <!-- 새 이미지 삭제 버튼 (옵션) -->
                <button @click="removeNewImage(index)">삭제</button>
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
