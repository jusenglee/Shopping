<script setup>
/* ---- Imports ---- */
import {ref, reactive, onMounted, computed} from 'vue';
import {useRoute} from 'vue-router';
import router from '@/router';
import api from '@/axios.js';

// Admin 사이드바
import AdminSideBar from '@/components/AdminSideBar.vue';

// vue-filepond + Plugins
import vueFilePond from 'vue-filepond';
import 'filepond/dist/filepond.min.css';
import 'filepond-plugin-image-preview/dist/filepond-plugin-image-preview.min.css';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type';
import FilePondPluginImagePreview from 'filepond-plugin-image-preview';
import CkEditorComponent from '@/components/CkEditorComponent.vue';

/* ---- FilePond Setup ---- */
const FilePond = vueFilePond(
    FilePondPluginFileValidateType,
    FilePondPluginImagePreview,
);
const pond = ref(null);
const initialFiles = ref([]);

function handleFilePondInit() {
  console.log('FilePond has been initialized.');
}

/* ---- /FilePond Setup ---- */

/// 이미지 로드
async function loadProductImages(itemId) {
  try {
    initialFiles.value = response.data.map(img => ({
      source: `http://localhost:8080/common/getImage/${img.id}`,
      options: {
        name: img.oriImgName,
      },
    }));
  } catch (error) {
    console.error('Failed to load product images:', error);
  }
}

/* ---- 이미지 로드 ---- */

/* ---- 메인 상품 데이터 ---- */
let url = '/admin/newItem';  // 기본 등록 URL, 수정 시 변경

// Vue가 추적할 반응형 객체(상품 기본 정보)
const itemData = reactive({
  id: '',
  itemNm: '',
  price: '',
  salePer: 0,
  itemDetail: '',
  category: 1,
  brand: 1,
  itemSellStatus: 'SELL',
});

/* ---- 상품 옵션 관련 ---- */
const itemOptions = ref([
  {color: '', size: '', material: '', stock: 1},
]);
const itemImages = ref([
  {id: '',  name: ''},
]);

// 옵션 추가
function addOption() {
  itemOptions.value.push({
    color: '',
    size: '',
    material: '',
    stock: 1,
  });
}

//상품 옵션 삭제
function removeOption(index) {
  if (itemOptions.value.length > 1) {
    itemOptions.value.splice(index, 1);
  }
}

// **1) 모든 옵션 재고 합계** - 여러 옵션의 stock 합산
const totalStock = computed(() => {
  return itemOptions.value.reduce((sum, opt) => sum + (opt.stock || 0), 0);
});
/* ---- /상품 옵션 관련 ---- */

/* ---- 정산 관련 ---- */
//  할인가 (discountedPrice): price * (1 - salePer/100)
const discountedPrice = computed(() => {
  const number = itemData.price.toLocaleString().replace(/,/g, '');
  const p = Number(number);
  const s = Number(itemData.salePer);
  const result = p * (1 - s / 100);
  const val = result > 0 ? Math.floor(result) : 0;
  // 소수점 필요 없으면 Math.floor 등 처리
  return val.toLocaleString().replace(/^0+|\D+/g, '').replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
});

//  총 예상 수익: (할인가) × (재고 합)
const expectedRevenue = computed(() => {
  const number = itemData.price.toLocaleString().replace(/,/g, '');
  const p = Number(number);
  const s = Number(totalStock.value);
  const result = p * s;
  return result.toLocaleString().replace(/^0+|\D+/g, '').replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
});
/* ---- /정산 관련 ---- */


/* ---- 공통 목록(브랜드, 카테고리, 색상, 사이즈, 소재) ---- */
const brandList = ref([]);
const categoriesList = ref([]);
const colorList = ref([]);
const sizesList = ref([]);
const materialsList = ref([]);
const errors = ref({});

// 여러 API 동시 호출
async function loadCommonLists() {
  try {
    const [
      brandRes,
      categoryRes,
      colorRes,
      sizeRes,
      materialRes,
    ] = await Promise.all([
      api.get('/common/brands'),
      api.get('/common/categories'),
      api.get('/common/colors'),
      api.get('/common/sizes'),
      api.get('/common/materials'),
    ]);

    // brand
    brandList.value = brandRes.data.map(item => ({
      value: item.id,
      text: item.name,
    }));
    // category
    categoriesList.value = categoryRes.data.map(item => ({
      value: item.id,
      text: item.cateName,
    }));
    // color, size, material (그대로 배열)
    colorList.value = colorRes.data;
    sizesList.value = sizeRes.data;
    materialsList.value = materialRes.data;

  } catch (error) {
    console.error('loadCommonLists error:', error);
  }
}

/* ---- /공통 목록(브랜드, 카테고리, 색상, 사이즈, 소재) ---- */

/* ---- 상품 저장(등록/수정) 로직 ---- */
async function submitData() {
  console.log(itemData.itemDetail);
  itemData.price = Number(itemData.price.toLocaleString().replace(/,/g, ''));

  // FilePond 인스턴스
  const pondInstance = pond.value;
  const files = pondInstance.getFiles();

  // FormData 생성
  const formData = new FormData();
  const finalData = {
    ...itemData,
    itemImgDtoList: [],
    options: itemOptions.value,
  };
  formData.append('itemFormDto', new Blob([JSON.stringify(finalData)], {type: 'application/json'}));

  // 2) 이미지 파일
  files.forEach(fileItem => {
    formData.append('images', fileItem.file, fileItem.file.name);
  });

  try {
    const response = await api.post(url, formData, {
      headers: {'Content-Type': 'multipart/form-data'},
    });

    alert(response.data.message || '상품이 성공적으로 등록되었습니다!');
    router.push('/admin/AdminPage');

  } catch (error) {
    console.error(error);

    if (error.response) {
      alert(error.response.data.message || '파일 업로드에 실패했습니다.');

      if (error.response.status === 400) {
        errors.value = error.response.data.errors || {};
        console.log('errors.value : ', errors.value);
      }

    } else {
      alert('서버와의 통신에 실패했습니다.');
    }
  }
}

/* ---- /상품 저장(등록/수정) 로직 ---- */

/* ---- onMounted 시 초기화 흐름 ---- */
const route = useRoute();
onMounted(async () => {
  // 1) 공통 목록 로드 (브랜드, 카테고리, 색상, 사이즈, 소재)
  await loadCommonLists();

  // 2) 수정 모드인지 확인
  const productId = route.params.id;
  // 예: /admin/Modified/:id 라는 라우트로 들어오는 경우
  if (productId) {
    // 수정 모드: 기존 정보 불러오기
    url = '/admin/modifyItem'; // 수정 URL로 변경
    try {
      const res = await api.get(`/getItemDetails/${productId}`);
      Object.assign(itemData, {
        id: res.data.id,
        itemNm: res.data.itemNm,
        price: res.data.price,
        itemDetail: res.data.itemDetail,
        category: res.data.category,
        brand: res.data.brand,
        itemSellStatus: res.data.itemSellStatus,
      });
        itemOptions.value = res.data.options;
        itemImages.value = res.data.options;

      // 이미지 로드
      await loadProductImages(productId);

    } catch (err) {
      console.error('Fetching item error: ', err);
    }
  }
});
</script>

<template>
  <div class="content-wrapper">
    <!-- 사이드바 -->
    <AdminSideBar />

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div class="row gx-4 gx-lg-5 align-items-center">
        <form
          id="form"
          class="main-form-content"
          enctype="multipart/form-data"
          @submit.prevent
        >
          <div class="col">
            <!-- 중앙 영역: 상품 기본정보 -->
            <div class="row">
              <div class="col">
                <div class="card">
                  <h4>제품 기본 옵션</h4><br>
                  <!-- 상품명 -->
                  <div
                    class="col-12"
                    style="margin-top: 10px;"
                  >
                    <label
                      class="form-label"
                      for="itemNm"
                    >Name</label>
                    <input
                      id="itemNm"
                      v-model="itemData.itemNm"
                      class="form-control"
                      name="itemNm"
                      placeholder="상품명을 입력해주세요"
                      type="text"
                    >
                    <div
                      v-if="errors.itemNm"
                      class="error-message"
                    >
                      {{ errors.itemNm }}
                    </div>
                  </div>

                  <!-- 카테고리 -->
                  <div
                    class="col-12"
                    style="margin-top: 10px;"
                  >
                    <label
                      class="form-label"
                      for="category"
                    >Category</label>
                    <select
                      v-model="itemData.category"
                      class="form-select form-select-sm"
                    >
                      <option
                        v-for="option in categoriesList"
                        :key="option.value"
                        :value="option.value"
                      >
                        {{ option.text }}
                      </option>
                    </select>
                  </div>

                  <!-- 브랜드 -->
                  <div
                    class="col-12"
                    style="margin-top: 10px;"
                  >
                    <label
                      class="form-label"
                      for="brand"
                    >Brand</label>
                    <select
                      v-model="itemData.brand"
                      class="form-select form-select-sm"
                    >
                      <option
                        v-for="option in brandList"
                        :key="option.value"
                        :value="option.value"
                      >
                        {{ option.text }}
                      </option>
                    </select>
                  </div>

                  <!-- 가격 -->
                  <div
                    class="col-12"
                    style="margin-top: 10px;"
                  >
                    <label
                      class="form-label"
                      for="price"
                    >Price</label>
                    <input
                      id="price"
                      v-model="itemData.price"
                      class="form-control"
                      name="price"
                      placeholder="상품 가격을 입력하세요."
                      type="text"
                      oninput="this.value = this.value.replace(/^0+|\D+/g, '').replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');"
                    >
                    <div
                      v-if="errors.price"
                      class="error-message"
                    >
                      {{ errors.price }}
                    </div>
                  </div>

                  <!-- 할인율 -->
                  <div
                    class="col-12"
                    style="margin-top: 10px;"
                  >
                    <label
                      class="form-label"
                      for="price"
                    >Sale percent</label>
                    <div class="input-group">
                      <input
                        id="salePer"
                        v-model="itemData.salePer"
                        class="form-control form-control-sm"
                        min="0"
                        max="99"
                        type="number"
                        name="salePer"
                      >
                      <span
                        id="basic-addon2"
                        class="input-group-text"
                      >%</span>
                    </div>
                    <div
                      v-if="errors.salePer"
                      class="error-message"
                    >
                      {{ errors.salePer }}
                    </div>
                  </div>


                  <!-- 총합 가격 -->
                  <div
                    class="col-12"
                    style="margin-top: 10px;"
                  >
                    <label
                      class="form-label"
                      for="discountedPrice"
                    >Total Price</label>
                    <input
                      id="price"
                      v-model="discountedPrice"
                      class="form-control form-control-sm"
                      min="0"
                      type="text"
                      name="discountedPrice"
                      disabled
                    >
                  </div>
                </div>
              </div>
            </div>
            <div class="card">
              <h4>제품 상세 옵션</h4>
              <!-- 옵션 추가 버튼 -->
              <div class="row">
                <div class="col align-self-end">
                  <button
                    class="button button--winona button--border-thin button--round-s"
                    data-text="Add Option"
                    type="button"
                    @click="addOption"
                  >
                    <span>Add Option</span>
                  </button>
                </div>
              </div>
              <br>

              <!-- 테이블을 반응형으로 감싸는 래퍼 -->
              <div
                class="table-responsive"
                style="margin-top: 5px; width: 100%"
              >
                <table class="table table-sm table-striped table-hover align-middle">
                  <thead class="table">
                    <tr>
                      <th scope="col">
                        Color
                      </th>
                      <th scope="col">
                        Size
                      </th>
                      <th scope="col">
                        Material
                      </th>
                      <th scope="col">
                        Stock
                      </th>
                      <th scope="col">
                        Remove
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr
                      v-for="(option, index) in itemOptions"
                      :key="index"
                    >
                      <!-- 색상 -->
                      <td>
                        <select
                          v-model="option.color"
                          class="form-select form-select-sm"
                        >
                          <option
                            disabled
                            value=""
                          >
                            색상 선택
                          </option>
                          <option
                            v-for="c in colorList"
                            :key="c"
                            :value="c"
                          >
                            {{ c }}
                          </option>
                        </select>
                      </td>
                      <!-- 사이즈 -->
                      <td>
                        <select
                          v-model="option.size"
                          class="form-select form-select-sm"
                        >
                          <option
                            disabled
                            value=""
                          >
                            사이즈 선택
                          </option>
                          <option
                            v-for="s in sizesList"
                            :key="s"
                            :value="s"
                          >
                            {{ s }}
                          </option>
                        </select>
                      </td>
                      <!-- 소재 -->
                      <td>
                        <select
                          v-model="option.material"
                          class="form-select form-select-sm"
                        >
                          <option
                            disabled
                            value=""
                          >
                            소재 선택
                          </option>
                          <option
                            v-for="m in materialsList"
                            :key="m"
                            :value="m"
                          >
                            {{ m }}
                          </option>
                        </select>
                      </td>
                      <!-- 재고수 -->
                      <td>
                        <input
                          v-model.number="option.stock"
                          class="form-control form-control-sm"
                          min="0"
                          type="number"
                        >
                      </td>
                      <!-- 삭제 버튼 -->
                      <td>
                        <button
                          class="button button--ujarak button--border-thin button--text-thick"
                          style="height: 100%; font-size: 12px"
                          type="button"
                          @click="removeOption(index)"
                        >
                          Remove
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
                <div
                  v-if="errors['options[0].color']"
                  class="error-message"
                >
                  {{ errors['options[0].color'] }}
                </div>
                <div
                  v-if="errors['options[0].size']"
                  class="error-message"
                >
                  {{ errors['options[0].size'] }}
                </div>
                <div
                  v-if="errors['options[0].material']"
                  class="error-message"
                >
                  {{ errors['options[0].material'] }}
                </div>
                <div
                  v-if="errors['options[0].stock']"
                  class="error-message"
                >
                  {{ errors['options[0].stock'] }}
                </div>
              </div>
            </div>


            <!-- 상품 상세 설정 -->
            <div
              class="col"
              style="margin-top: 15px;"
            >
              <div class="card">
                <h4>상품 상세 페이지 입력</h4>
                <div style="width: 100%;">
                  <CkEditorComponent
                    :detail="itemData.itemDetail"
                    @update:detail="(val) => (itemData.itemDetail = val)"
                  />
                </div>
                <div
                  v-if="errors.itemDetail"
                  class="error-message"
                >
                  {{ errors.itemDetail }}
                </div>
              </div>
            </div>
          </div>

          <!-- 우측 옵션 테이블 -->
          <aside class="sticky-aside">
            <!-- 이미지 업로드(FilePond) -->
            <div class="card">
              <h4>상품 이미지 추가</h4>
              <div style="width: 100%;">
                <FilePond
                  ref="pond"
                  :files="initialFiles"
                  accepted-file-types="image/jpeg, image/png"
                  allow-multiple="true"
                  label-idle="Drag & Drop your image or <span class='filepond--label-action'>Browse</span>"
                  name="images"
                  @init="handleFilePondInit"
                />
              </div>
            </div>

            <div class="card">
              <h4>명세서 검토</h4><br>
              <div
                class="table-responsive"
                style="margin-top: 5px; width: 100% "
              >
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th scope="col">
                        최종가
                      </th>
                      <th scope="col">
                        총 재고
                      </th>
                      <th scope="col">
                        예상 총 수익
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>
                        {{ discountedPrice }} 원
                      </td>
                      <td>
                        {{ totalStock }} 개
                      </td>
                      <td>
                        {{ expectedRevenue }} 원
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <hr class="my-4">
              <!-- 등록/수정 버튼 -->
              <button
                id="formBtn"
                class="w-100 btn btn-primary btn-lg mb-3"
                type="button"
                @click="submitData"
              >
                상품 저장
              </button>
            </div>
          </aside>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-form-content {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
}

.sticky-aside {
  width: 500px;
  margin-left: 10px;
  position: sticky;
  top: 20px; /* 화면 상단에서 20px 떨어진 지점에 고정 */
  z-index: 10; /* 다른 요소 위에 위치 */
}

</style>
