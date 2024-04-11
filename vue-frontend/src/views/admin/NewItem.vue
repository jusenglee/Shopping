<script setup>
import {computed, getCurrentInstance, onMounted, ref} from 'vue';
import AdminSideBar from "@/components/AdminSideBar.vue";
import api from "@/axios.js";
import {useRoute} from 'vue-router';
import vueFilePond from "vue-filepond";
import "filepond/dist/filepond.min.css";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.css";
import FilePondPluginFileValidateType from "filepond-plugin-file-validate-type";
import FilePondPluginImagePreview from "filepond-plugin-image-preview";
import {useStore} from 'vuex';

const store = useStore();
const token = computed(() => store.state.userToken);
const instance = getCurrentInstance();
const $getCookie = instance.appContext.config.globalProperties.$getCookie;
// FilePond 플러그인을 사용하여 Vue 컴포넌트 생성
const FilePond = vueFilePond(
    FilePondPluginFileValidateType,
    FilePondPluginImagePreview
);
const initialFiles = ref([]); // 초기 파일 목록을 저장할 반응형 데이터
const pond = ref(null);

// @init 이벤트 핸들러
function handleFilePondInit() {
  console.log("FilePond has been initialized.");
}

// FilePond 설정 객체
const serverConfig = computed(() => ({
  process(fieldName, file, metadata, load, error, progress, abort) {
    const formData = new FormData();
    formData.append(fieldName, file, file.name);

    // 비동기 요청을 사용하여 파일 업로드를 수행
    fetch('http://localhost:8081/common/saveImage', {
      method: 'POST',
      headers: {
        // 토큰과 다른 헤더 정보를 설정
        'Authorization': `Bearer ${token.value}`,
        'X-XSRF-TOKEN': $getCookie('XSRF-TOKEN')
      },
      body: formData,
    })
        .then(response => {
          // HTTP 상태 코드가 200-299 범위인지 확인
          if (response.ok) {
            return response.json(); // 서버로부터 반환된 JSON 응답을 파싱
          }
          // 응답이 성공적이지 않으면 에러를 던짐
          throw new Error(response);
        })
        .then(data => {
          console.log(data); // 성공 시 서버 응답을 콘솔에 출력
          itemData.value.itemImgDtoList.push(data)
          console.log("itemData.value.itemImgDtoList : ", itemData.value.itemImgDtoList)
          load(data.imgUrl); // 예시: 서버로부터 반환된 파일 ID를 load 함수에 전달
        })
        .catch(err => {
          console.error('Upload error:', err);
          error(err.message);
        });

    // fetch 요청의 취소를 처리하는 로직을 구현
    return {
      abort: () => {
        // 요청 취소 로직 (예: AbortController를 사용하는 경우 abort() 호출)
        abort();
      }
    };
  },
  load: async (source, load, error) => {
    try {
      console.log("load 호출! ID 번호 : ", source);
      const response = await api.get(`${source}`, {
        responseType: 'blob',
      });
      load(response.data);
    } catch (err) {
      error(err.message);
    }
  },
}));

async function loadProductImages(itemId) {
  try {
    const response = await api.get(`/common/loadImage/${itemId}`);
    initialFiles.value = response.data.map(img => ({
      source: img.imgUrl, // 예시: img 객체 내의 imgUrl 프로퍼티를 사용
      options: {
        name: img.oriImgName,
        fileName: img.imgName,
        repimgYn: img.repimgYn,
        type: 'local',
      },
    })); // 반응형 데이터를 업데이트하여 FilePond에 파일을 설정
  } catch (error) {
    console.error('Failed to load product images:', error);
  }
}

let url = "/admin/newItem" //상cd v품 등록, 수정에 따른 url 변경을 위한 변수
const itemData = ref({
  itemId: '',
  itemNm: '',
  price: '',
  stockNumber: '',
  itemDetail: '',
  category: 1,
  brand: 1,
  itemSellStatus: 'SELL',
  itemImgDtoList: [], // 사용자가 선택한 새 파일
});
const errors = ref({});
const brandList = ref([]);
const categoriesList = ref([]); // 카테고리 목록을 저장할 반응형 참조
// API로부터 브랜드 목록을 가져오는 함수
const getBrandAndCategories = () => {
  api.get('/admin/brands')
      .then(response => {
        brandList.value = response.data.map(item => ({
          value: item.id, // API 응답 구조에 따라 조정 필요
          text: item.name // API 응답 구조에 따라 조정 필요
        }));
      })
      .catch(error => {
        console.error('error', error);
      });
  api.get('/admin/categories')
      .then(response => {
        categoriesList.value = response.data.map(item => ({
          value: item.id, // API 응답 구조에 따라 조정 필요
          text: item.cateName // API 응답 구조에 따라 조정 필요
        }));
      })
      .catch(error => {
        console.error('error', error);
      });
};

// 서버로 상품 데이터를 보내는 함수
const submitData = () => {
  const itemDataJson = JSON.stringify(itemData.value);
  console.log(itemDataJson)
  api.post(url, itemDataJson, {
    headers: {
      'Content-Type': 'application/json',
    },
  }).then(response => {
    alert(response.data);
    //router.push('/admin/AdminPage');
  }).catch(error => {
    console.log("error : ", error);
    if (error.request.status == '400') {
      errors.value = error.response.data;
      console.log("errors.value : ", errors.value);
    }
  });
}

onMounted(getBrandAndCategories);
//상품 수정 URL을 통해 접근시
const route = useRoute(); // vue-router 4.x에서 제공하는 useRoute 컴포지션 API 사용
onMounted(() => {
  const productId = route.params.id; // params에서 직접 접근
  const expectedPath = `/admin/Modified/${productId}`;
  if (route.path === expectedPath) {
    api.get(`/item/${productId}`)
        .then(response => {
          itemData.value = response.data.itemData;
          loadProductImages(productId);
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
              <div v-if="errors.itemNm" class="error-message">{{ errors.itemNm }}</div>
            </div>

            <div class="col-12">
              <label class="form-label" for="itemDetail">상품 설명</label>
              <input id="itemDetail" v-model="itemData.itemDetail" class="form-control" name="itemDetail"
                     placeholder="상품 설명을 입력하세요." type="text">
              <div v-if="errors.itemDetail" class="error-message">{{ errors.itemDetail }}</div>
            </div>

            <div class="col-12">
              <label class="form-label" for="price">상품 가격</label>
              <input id="price" v-model="itemData.price" class="form-control" name="price" placeholder="상품 가격을 입력하세요."
                     type="number">
              <div v-if="errors.price" class="error-message">{{ errors.price }}</div>
            </div>

            <div class="col-12">
              <label class="form-label" for="stock">상품 재고</label>
              <input id="stock" v-model="itemData.stockNumber" class="form-control" name="stock"
                     placeholder="상품 재고를 입력하세요." type="number">
              <div v-if="errors.stock" class="error-message">{{ errors.stock }}</div>
            </div>

            <div class="col-12">
              <label class="form-label" for="image" style="margin-top: 10px">
                상품이미지 추가</label>

              <FilePond
                  ref="pond"
                  :files="initialFiles"
                  :server="serverConfig"
                  accepted-file-types="image/jpeg, image/png"
                  allow-multiple="true"
                  label-idle='Drag & Drop your image or <span class="filepond--label-action">Browse</span>'
                  @init="handleFilePondInit"
              />
              <div v-if="errors.itemImgDtoList" class="error-message">{{ errors.itemImgDtoList }}</div>
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
