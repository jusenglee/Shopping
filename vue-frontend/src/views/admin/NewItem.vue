<script setup>
import AdminSideBar from "@/components/AdminSideBar.vue";
import {computed, getCurrentInstance, onMounted, reactive, ref} from 'vue';
import router from "@/router";
import {useRoute} from 'vue-router';
import {useStore} from 'vuex';
import api from "@/axios.js";
import vueFilePond from "vue-filepond";
import "filepond/dist/filepond.min.css";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.css";
import FilePondPluginFileValidateType from "filepond-plugin-file-validate-type";
import FilePondPluginImagePreview from "filepond-plugin-image-preview";
import TagsInput from "@/components/TagsInput.vue";

const store = useStore();
const token = computed(() => store.state.userToken);
const instance = getCurrentInstance();
const $getCookie = instance.appContext.config.globalProperties.$getCookie;


/* 이미지 관련 */
// FilePond 플러그인을 사용하여 Vue 컴포넌트 생성
const FilePond = vueFilePond(
    FilePondPluginFileValidateType,
    FilePondPluginImagePreview
);

const initialFiles = ref([]); // 초기 파일 목록을 저장할 반응형 데이터
const pond = ref(null);

function handleFilePondInit() { // @init 이벤트 핸들러
  console.log("FilePond has been initialized.");
}

const serverConfig = computed(() => ({// FilePond 설정 객체
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
          itemData.itemImgDtoList.push(data)
          console.log("itemData.itemImgDtoList : ", itemData.itemImgDtoList)
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

async function loadProductImages(itemId) { //이미지 불러오기
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

/* //이미지 관련 */

let url = "/admin/newItem" //새 상품 등록 URL, 수정에 따른 url 변경을 위한 변수

const itemData = reactive({ //상품 등록 Form 관련 객체
  itemId: '',
  itemNm: '',
  price: '',
  inventory: [],
  itemDetail: '',
  category: 1,
  brand: 1,
  itemSellStatus: 'SELL',
  itemImgDtoList: [], // 사용자가 선택한 새 파일
  color: [],
  fabric: []
});

const brandList = ref([]); // 브랜드 목록을 저장할 반응형 참조
const categoriesList = ref([]); // 카테고리 목록을 저장할 반응형 참조
const errors = ref({});

const getBrandAndCategories = () => { // 카테고리와 브랜드 목록 로드
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

/* 재고 관련 */
const addInventoryItem = () => { // 재고 input 칸 추가
  if (itemData.inventory.length < 4) {  // 최대 4개까지만 추가 가능
    itemData.inventory.push({size: '', stock: 0});
  } else {
    errors.value.inventory = '사이즈는 최대 4개까지만 설정 가능합니다.'
  }
};

function removeInventoryItem(index) { // 재고 input칸 제거
  itemData.inventory.splice(index, 1);
  errors.value.inventory = null;
}

const sizes = ['S', 'M', 'L', 'XL'];
const availableSizes = computed(() => { // 사이즈 목록 동적 출력
  const selectedSizes = itemData.inventory.map(item => item.size);
  return size => sizes.filter(s => s === size || !selectedSizes.includes(s));
});
/* // 재고 관련 */

const updateTags = (updatedValue, targetState) => {
  targetState.value = updatedValue;
  console.log("targetState : ", targetState.value);
};


const submitData = () => { // 서버로 상품 데이터를 보내는 함수
  console.log("start submitData 동작");
  const itemDataJson = JSON.stringify(itemData.value);
  api.post(url, itemDataJson, {
    headers: {
      'Content-Type': 'application/json',
    },
  }).then(response => {
    alert(response.data);
    router.push('/admin/AdminPage');
  }).catch(error => {
    alert(error.request.data);
    if (error.request.status == '400') {
      errors.value = error.response.data;
      console.log("errors.value : ", errors.value);
    }
  });
}


onMounted(getBrandAndCategories); // 카테고리 및 브랜드 목록 호출
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
        <form id="form" class="main-form-content" enctype="multipart/form-data" @submit.prevent>
          <div class="col">
            <!-- 중앙 콘텐츠 -->
            <div class="row">
              <div class="col">
                <div class="card">

                  <div class="col-12" style="margin-top: 10px;">
                    <label class="form-label" for="itemNm">상품명</label>
                    <input id="itemNm" v-model="itemData.itemNm" class="form-control" name="itemNm"
                           placeholder="상품명을 입력해주세요"
                           type="text">
                    <div v-if="errors.itemNm" class="error-message">{{ errors.itemNm }}</div>
                  </div>

                  <div class="col-12" style="margin-top: 10px;">
                    <label class="form-label" for="brand">카테고리</label>
                    <select v-model="itemData.category" class="form-select form-select-sm">
                      <option v-for="option in categoriesList" :key="option.value" :value="option.value">
                        {{ option.text }}
                      </option>
                    </select>
                  </div>

                  <div class="col-12" style="margin-top: 10px;">
                    <label class="form-label" for="category">브랜드</label>
                    <select v-model="itemData.brand" class="form-select form-select-sm">
                      <option v-for="option in brandList" :key="option.value" :value="option.value">
                        {{ option.text }}
                      </option>
                    </select>
                  </div>

                  <div class="col-12" style="margin-top: 10px;">
                    <label class="form-label" for="price">상품 가격</label>
                    <input id="price" v-model="itemData.price" class="form-control" name="price"
                           placeholder="상품 가격을 입력하세요."
                           type="number">
                    <div v-if="errors.price" class="error-message">{{ errors.price }}</div>
                  </div>

                  <div class="col-12" style="margin-top: 15px;">
                    <div class="row row-cols-auto">
                      <label class="form-label">상품 재고</label>
                      <button class="button button--winona button--border-thin button--round-s"
                              data-text="Add Size"
                              type="button" @click="addInventoryItem"><span>Add Stock</span></button>
                    </div>
                    <div class="row" style="margin-top: 5px;">
                      <div v-for="(item, index) in itemData.inventory" :key="index" class="row"
                           style="margin-top: 5px;">
                        <div class="col-6">
                          <div class="form-floating">
                            <select v-model="item.size" class="form-select" @change="updateAvailableSizes">
                              <option v-for="size in availableSizes(item.size)" :key="size" :value="size">
                                {{ size }}
                              </option>
                            </select>
                            <label>Size</label>
                          </div>
                        </div>
                        <div class="col-3">
                          <div class="form-floating">
                            <input v-model.number="item.stock" class="form-control" min="0"
                                   placeholder="Stock Quantity"
                                   style="height: 40px;" type="number"/>
                            <label>stock</label>
                          </div>
                        </div>
                        <div class="col-3">
                          <button
                              class="button button--ujarak button--border-thin button--text-thick"
                              style="height: 100%; font-size: 12px"
                              type="button" @click="removeInventoryItem(index)">Remove
                          </button>
                        </div>
                      </div>
                      <div v-if="errors.inventory" class="error-message">{{ errors.inventory }}</div>
                    </div>
                  </div>

                </div>
              </div>
            </div>

            <div class="col">
              <div class="card">
                <h4>상품 이미지 추가</h4>
                <div style="width: 100%">
                  <FilePond
                      ref="pond"
                      :files="initialFiles"
                      :server="serverConfig"
                      accepted-file-types="image/jpeg, image/png"
                      allow-multiple="true"
                      label-idle='Drag & Drop your image or <span class="filepond--label-action">Browse</span>'
                      @init="handleFilePondInit"
                  />
                </div>
              </div>
            </div>
            <div class="col">
              <div class="card">

              </div>
            </div>
            <hr class="my-4">
            <button id="formBtn" class="w-100 btn btn-primary btn-lg mb-3" type="button" @click="submitData">상품 등록
            </button>
          </div>
          <!-- 사이드바 시작 -->
          <aside class="card" style="width: 25%; margin-left: 20px; padding: 2em 2em 2em 2em">
            <h4>제품 상세 정보</h4>

            <div class="col-10">
              <div>
                <label class="form-label" for="fabric">제품 소재</label>
                <tags-input
                    :initial-tags="itemData.fabric"
                    @update:tags="tags => updateTags(tags, itemData.fabric)"
                />
              </div>
            </div>

            <div class="col-10">
              <div>
                <label class="form-label" for="color">제품 색상</label>
                <tags-input
                    :initial-tags="itemData.color"
                    @update:tags="tags => updateTags(tags, itemData.color)"
                />
              </div>
            </div>

            <div class="col-10">
              <label class="form-label" for="sizeOf">치수 설명</label>
              <input id="sizeOf" class="form-control" type="text" value="사이즈 표 참조">
            </div>

            <div class="col-10">
              <label class="form-label" for="made">제조자</label>
              <input id="made" class="form-control" type="text">
            </div>

            <div class="col-10">
              <label class="form-label" for="madeNation">제조국</label>
              <input id="madeNation" class="form-control" type="text">
            </div>

            <div class="col-10" style="border-bottom: #3d3d3d">
              <label class="form-label" for="text">세탁방법 및 취급시 주의사항</label>
              <input id="text" class="form-control" type="text">
            </div>

            <div class="col-10">
              <label class="form-label" for="orderDate">제조연월</label>
              <input id="orderDate" class="form-control" type="text">
            </div>

            <div class="col-10">
              <label class="form-label" for="text2">품질 보증 기준</label>
              <input id="text2" class="form-control" type="text">
            </div>

            <div class="col-10">
              <label class="form-label" for="customerService">A/S 책임자와 전화번호</label>
              <input id="customerService" class="form-control" type="text">
            </div>
          </aside>
          <!-- 사이드바 끝 -->
        </form>
        <div style="text-align: center">
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-form-content {
  display: flex;
}


</style>
