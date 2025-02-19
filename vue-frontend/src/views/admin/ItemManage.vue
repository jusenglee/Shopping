<script setup>
import {onMounted, ref} from 'vue';
import api from '@/axios';
import AdminSideBar from '@/components/AdminSideBar.vue';

const products = ref([]);

// 데이터를 가져오는 함수
const getItem = () => {
  api.get('/admin/ItemManage/sellerItemList').then(response => {
    products.value = response.data;
    console.log('products List : ', products.value);
  }).catch(error => {
    console.log(error);
  });
};
const getStatusClass = (status) => {
  switch (status) {
    case 'SELL':
      return 'badge badge-sale';
    case 'new':
      return 'badge badge-new';
    case 'STOPPED':
      return 'badge badge-hot';
    case 'top':
      return 'badge badge-top';
    default:
      return '';
  }
};

async function deleteItem(itemId) {
  try {
    await api.delete(`/admin/manage/delete/${itemId}`);
    getItem();
  } catch (error) {
    console.log(error.response);
    alert(error.response.data.message)
  }
}

async function stopSellingIterm(itemId) {
  try {
    await api.put(`/admin/manage/stopSellItem/${itemId}`);
    getItem();
  } catch (error) {
    console.log(error.response);
    alert(error.response.data.message)
  }
}

onMounted(getItem);

function totalStockFunction(options) {
  console.log(options)
  let totalStock = 0;

  if (Array.isArray(options) && options.length > 0) {
    options.forEach((itemOption) => {

      totalStock += itemOption.stock;
    });
  }

  return totalStock;
}

</script>

<template>
  <div class="content-wrapper">
    <!-- 사이드바 -->
    <AdminSideBar />

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <table class="table">
        <colgroup>
          <col>
          <col>
          <col>
          <col>
          <col>
          <col>
          <col>
          <col>
          <col style="width:20%">
        </colgroup>
        <thead class="thead-light">
          <tr>
            <th>No</th>
            <th>대표 이미지</th>
            <th>상태</th>
            <th>상품명</th>
            <th>카테고리</th>
            <th>가격</th>
            <th>총재고</th>
            <th>등록일</th>
            <th>처리</th>
          </tr>
        </thead>
        <tbody id="productTable">
          <tr
            v-if="products.length == 0"
          >
            <td colspan="9">
              등록된 상품이 없습니다.
            </td>
          </tr>
          <tr
            v-for="(product, index) in products"
            :key="product.id"
          >
            <td>{{ index + 1 }}</td>
            <td>
              <img
                v-if="product.itemImgList.length > 0"
                :src="(`http://localhost:8080/common/getImage/${product.itemImgList[0].id}`)"
                alt="Illustration"
                style="width: 50px; height: 50px;"
              >
            </td>
            <td><span :class="getStatusClass(product.itemSellStatus)">{{ product.itemSellStatus }}</span></td>
            <td>{{ product.itemNm }}</td>
            <td>{{ product.cateName }}</td>
            <td>{{ product.price }}</td>
            <td>{{ totalStockFunction(product.itemOptionList) }}</td>
            <td>{{ $formatDate(product.createdDate) }}</td>
            <td>
              <div
                class="row"
                style="display: flex; justify-content: center;"
              >
                <router-link
                  :to="`/admin/Modified/${product.id}`"
                  class="button"
                  data-text="Modified"
                  style="width:80px; height: 30px; margin-right: 5px; display: flex; align-items: center; justify-content: center;"
                >
                  <span><i
                    class="bi bi-check-circle-fill"
                    style="margin-right: 5px"
                  />Modify</span>
                </router-link>

                <button
                  v-if="product.itemSellStatus !== 'STOPPED'"
                  class="button"
                  data-text="STOPPED"
                  style="width:80px; height: 30px; margin-right: 5px; display: flex; align-items: center; justify-content: center;"
                  @click="stopSellingIterm(product.id)"
                >
                  <span><i
                    class="bi bi-check-circle-fill"
                    style="margin-right: 5px"
                  />STOPPED</span>
                </button>

                <button
                  class="button"
                  data-text="Delete"
                  style="width:80px; height: 30px; display: flex; align-items: center; justify-content: center;"
                  @click="deleteItem(product.id)"
                >
                  <span><i
                    class="bi bi-check-circle-fill"
                    style="margin-right: 5px"
                  />Delete</span>
                </button>
              </div>
            </td>
            <!-- 추가 데이터 -->
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.button {
  text-align: center; /* 가로 방향 가운데 정렬 */
  vertical-align: middle; /* 세로 방향 가운데 정렬 */
  padding: 10px 15px;
  width: 100%;
  height: 100%;
  white-space: nowrap;
  border-radius: 10px;
  text-decoration: none;
  border: none;
  background: #5e595a;
  color: #ccc;
  outline: none;
  font-family: 'Poppins', sans-serif;
  font-weight: bold;
  font-size: 12px;
}

.button:focus,
.button:hover {
  background: #617e9f;
}
</style>
