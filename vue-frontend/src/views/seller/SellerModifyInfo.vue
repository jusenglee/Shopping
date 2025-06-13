<script setup>

import {onMounted, ref} from 'vue';
import AdminSideBar from '@/components/AdminSideBar.vue';
import api from "@/axios";
import {useRoute} from 'vue-router';

const route = useRoute();
const currentTab = ref("basic");
const errors = ref({});

//초기 로딩시 사용자 정보 가져오기
const getUserData = () => {
  api.get('/seller/sellerInfo')
      .then(response => {
        for (let key in response.data) {
          if (Object.prototype.hasOwnProperty.call(basicInfo.value, key)) {
            basicInfo.value[key] = response.data[key];
          }
        }
        console.log(basicInfo.value);
      })
      .catch(error => {
        console.log(error);
      });
};

const tabs = [
  {id: "basic", name: "기본정보"},
  {id: "business", name: "사업자정보"},
  {id: "security", name: "보안설정"},
];

const profileImage = ref("");
const fileInput = ref(null);
const businessLicenseInput = ref(null);
const onlineSalesLicenseInput = ref(null);

const basicInfo = ref({
  id:"",
  email:"",
  name:"",
  phone:"",
  zipCode:"",
  roadAddress:"",
  detailAddress:"",
  companyName:"",
  businessNumber:"",
  ceoName:"",
  businessLicenseUrl:"",
  onlineSalesLicenseUrl:"",
  bank:"",
  accountNumber:"",
  passWord:"",
}); // 사용자 기본 정보

const showModal = ref(false);
const showAlert = ref(false);
const modalTitle = ref("");
const modalMessage = ref("");

const handleImageUpload = () => {
  fileInput.value && fileInput.value.click();
};

const onImageChange = (event) => {
  const target = event.target;
  if (target.files && target.files[0]) {
    const file = target.files[0];
    const reader = new FileReader();
    reader.onload = (e) => {
      profileImage.value = e.target.result;
    };
    reader.onerror = (error) => {
      console.error("파일 읽기 중 에러 발생:", error);
    };
    reader.readAsDataURL(file);
  }
};

const handleBusinessLicenseUpload = () => {
  businessLicenseInput.value && businessLicenseInput.value.click();
};

const onBusinessLicenseChange = (event) => {
  const target = event.target;
  if (target.files && target.files[0]) {
    basicInfo.value.businessLicense = target.files[0].name;
  }
};

const handleOnlineSalesLicenseUpload = () => {
  onlineSalesLicenseInput.value && onlineSalesLicenseInput.value.click();
};

const onOnlineSalesLicenseChange = (event) => {
  const target = event.target;
  if (target.files && target.files[0]) {
    basicInfo.value.onlineSalesLicense = target.files[0].name;
  }
};

const openPostcode = () => {
  new window.daum.Postcode({
    oncomplete: (data) => {
      basicInfo.value.zipCode = data.zonecode;
      basicInfo.value.roadAddress = data.roadAddress;
      basicInfo.value.detailAddress = data.jibunAddress;

      let extraAddr = '';
      if (data.bname && /[동|로|가]$/g.test(data.bname)) {
        extraAddr += data.bname;
      }
      if (data.buildingName && data.apartment === 'Y') {
        extraAddr += extraAddr ? `, ${data.buildingName}` : data.buildingName;
      }
      if (extraAddr) {
        basicInfo.value.extraAddress = `(${extraAddr})`;
      }
    },
  }).open();
};
const save = () => {
  console.log(basicInfo.value);
  api.post('/seller/sellerInfoUpdate', basicInfo.value).then(() => {
    showModal.value = true;
    modalTitle.value = "저장 완료";
    modalMessage.value = "변경사항이 성공적으로 저장되었습니다.";
  }).catch(error => {
    errors.value = error.response.data.errors || {};
    console.log(error);
  });
};

const cancel = () => {
  showModal.value = true;
  modalTitle.value = "취소 확인";
  modalMessage.value = "변경사항을 취소하시겠습니까?";
};

const closeModal = () => {
  showModal.value = false;
};

onMounted(() => {
  getUserData();
  console.log(route.query)
  if (route.query.showAlert === 'true') {
    showAlert.value = true;
  }
});
</script>

<template>
  <div class="content-wrapper">
    <!-- 사이드바 -->
    <AdminSideBar />

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div class="container-fluid">
        <div class="row">
          <!-- 메인 콘텐츠 영역 -->
          <div class="col">
            <div
              v-if="showAlert"
              class="alert alert-success"
              role="alert"
              tabindex="-1"
            >
              <h4 class="alert-heading">
                반갑습니다!
              </h4>
              <p>저희 홈페이지의 판매자로써 활동해주시는것에 감사드립니다! 첫 활동을 위하여 아래 '내 정보'를 등록해주세요!</p>
              <hr>
              <p class="mb-0">
                Thank you for being a seller on our website! Please register 'My Information' below for your first activity!
              </p>
            </div>
            <!-- 탭 메뉴 (부트스트랩 Nav 탭) -->
            <ul class="nav nav-tabs">
              <li
                v-for="tab in tabs"
                :key="tab.id"
                class="nav-item"
              >
                <a
                  href="#"
                  class="nav-link"
                  :class="{ active: currentTab === tab.id }"
                  @click.prevent="currentTab = tab.id"
                >
                  {{ tab.name }}
                </a>
              </li>
            </ul>
            <!-- 기본정보 탭 -->
            <div
              v-if="currentTab === 'basic'"
              class="card my-custom-card mb-4"
            >
              <div class="card-body">
                <div class="row">
                  <div class="col-md-4 text-center">
                    <img
                      :src="profileImage || 'https://public.readdy.ai/ai/img_res/50aaff8199ee21792afcb8640efa06fc.jpg'"
                      alt="프로필 이미지"
                      class="img-fluid rounded-circle mb-2"
                      style="width: 150px; height: 150px; object-fit: cover;"
                    >
                    <br>
                    <button
                      class="btn btn-link"
                      @click="handleImageUpload"
                    >
                      이미지 변경
                    </button>
                    <input
                      ref="fileInput"
                      type="file"
                      class="d-none"
                      accept="image/*"
                      @change="onImageChange"
                    >
                  </div>
                  <div class="col-md-8">
                    <div class="mb-3 row">
                      <label class="col-sm-3 col-form-label">이름 <span class="text-danger">*</span></label>
                      <div class="col-sm-9">
                        <input
                          v-model="basicInfo.name"
                          type="text"
                          class="form-control"
                          placeholder="실명을 입력해주세요"
                        >
                        <div
                          v-if="errors.name"
                          class="error-message"
                        >
                          {{ errors.name }}
                        </div>
                      </div>
                    </div>
                    <div class="mb-3 row">
                      <label class="col-sm-3 col-form-label">연락처 <span class="text-danger">*</span></label>
                      <div class="col-sm-9">
                        <input
                          v-model="basicInfo.phone"
                          type="tel"
                          class="form-control"
                          placeholder="010-0000-0000"
                        >
                        <div
                          v-if="errors.phone"
                          class="error-message"
                        >
                          {{ errors.phone }}
                        </div>
                      </div>
                    </div>
                    <div class="mb-3 row">
                      <label class="col-sm-3 col-form-label">이메일 <span class="text-danger">*</span></label>
                      <div class="col-sm-9">
                        <input
                          v-model="basicInfo.email"
                          type="email"
                          class="form-control"
                          placeholder="example@domain.com"
                        >
                        <div
                          v-if="errors.email"
                          class="error-message"
                        >
                          {{ errors.email }}
                        </div>
                      </div>
                    </div>
                    <div class="mb-3 row">
                      <label class="col-sm-3 col-form-label">주소 <span class="text-danger">*</span></label>
                      <div class="col-sm-9">
                        <div class="input-group mb-2">
                          <input
                            v-model="basicInfo.zipCode"
                            type="text"
                            class="form-control"
                            placeholder="우편번호"
                            readonly
                          >
                          <button
                            class="btn btn-outline-secondary"
                            type="button"
                            @click="openPostcode"
                          >
                            우편번호 검색
                          </button>
                        </div>
                        <div
                          v-if="errors.zipCode"
                          class="error-message"
                        >
                          {{ errors.zipCode }}
                        </div>
                        <input
                          v-model="basicInfo.roadAddress"
                          type="text"
                          class="form-control mb-2"
                          placeholder="기본주소"
                        >
                        <div
                          v-if="errors.roadAddress"
                          class="error-message"
                        >
                          {{ errors.roadAddress }}
                        </div>
                        <input
                          v-model="basicInfo.detailAddress"
                          type="text"
                          class="form-control"
                          placeholder="상세주소"
                        >
                        <div
                          v-if="errors.detailAddress"
                          class="error-message"
                        >
                          {{ errors.detailAddress }}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 사업자정보 탭 -->
            <div
              v-if="currentTab === 'business'"
              class="card my-custom-card mb-4"
            >
              <div class="card-body">
                <div class="mb-3 row">
                  <label class="col-sm-3 col-form-label">상호명 <span class="text-danger">*</span></label>
                  <div class="col-sm-9">
                    <input
                      v-model="basicInfo.companyName"
                      type="text"
                      class="form-control"
                      placeholder="상호명을 입력해주세요"
                    >
                    <div
                      v-if="errors.companyName"
                      class="error-message"
                    >
                      {{ errors.companyName }}
                    </div>
                  </div>
                </div>
                <div class="mb-3 row">
                  <label class="col-sm-3 col-form-label">사업자등록번호 <span class="text-danger">*</span></label>
                  <div class="col-sm-9">
                    <input
                      v-model="basicInfo.businessNumber"
                      type="text"
                      class="form-control"
                      placeholder="000-00-00000"
                    >
                    <div
                      v-if="errors.businessNumber"
                      class="error-message"
                    >
                      {{ errors.companyName }}
                    </div>
                  </div>
                </div>
                <div class="mb-3 row">
                  <label class="col-sm-3 col-form-label">대표자명 <span class="text-danger">*</span></label>
                  <div class="col-sm-9">
                    <input
                      v-model="basicInfo.ceoName"
                      type="text"
                      class="form-control"
                      placeholder="대표자명을 입력해주세요"
                    >
                    <div
                      v-if="errors.ceoName"
                      class="error-message"
                    >
                      {{ errors.ceoName }}
                    </div>
                  </div>
                </div>
                <div class="mb-3">
                  <label class="form-label">사업자등록증</label>
                  <div class="input-group">
                    <input
                      type="text"
                      class="form-control"
                      :value="basicInfo.businessLicense || '파일을 선택해주세요'"
                      readonly
                    >
                    <button
                      class="btn btn-outline-secondary"
                      type="button"
                      @click="handleBusinessLicenseUpload"
                    >
                      파일 선택
                    </button>
                  </div>
                  <input
                    ref="businessLicenseInput"
                    type="file"
                    class="d-none"
                    accept=".pdf,.jpg,.jpeg,.png"
                    @change="onBusinessLicenseChange"
                  >
                </div>
                <div class="mb-3">
                  <label class="form-label">통신판매업신고증</label>
                  <div class="input-group">
                    <input
                      type="text"
                      class="form-control"
                      :value="basicInfo.onlineSalesLicense || '파일을 선택해주세요'"
                      readonly
                    >
                    <button
                      class="btn btn-outline-secondary"
                      type="button"
                      @click="handleOnlineSalesLicenseUpload"
                    >
                      파일 선택
                    </button>
                  </div>
                  <input
                    ref="onlineSalesLicenseInput"
                    type="file"
                    class="d-none"
                    accept=".pdf,.jpg,.jpeg,.png"
                    @change="onOnlineSalesLicenseChange"
                  >
                </div>
                <div class="mb-3 row">
                  <label class="col-sm-3 col-form-label">정산계좌 정보 <span class="text-danger">*</span></label>
                  <div class="col-sm-9">
                    <div class="row">
                      <div class="col-md-6">
                        <select
                          v-model="basicInfo.bank"
                          class="form-select"
                        >
                          <option
                            value=""
                            selected
                          >
                            은행선택
                          </option>
                          <option value="KB">
                            KB국민은행
                          </option>
                          <option value="shinhan">
                            신한은행
                          </option>
                          <option value="woori">
                            우리은행
                          </option>
                          <option value="hana">
                            하나은행
                          </option>
                        </select>
                        <div
                          v-if="errors.bank"
                          class="error-message"
                        >
                          {{ errors.bank }}
                        </div>
                      </div>
                      <div class="col-md-6">
                        <input
                          v-model="basicInfo.accountNumber"
                          type="text"
                          class="form-control"
                          placeholder="계좌번호를 입력해주세요"
                        >
                        <div
                          v-if="errors.accountNumber"
                          class="error-message"
                        >
                          {{ errors.accountNumber }}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 보안설정 탭 -->
            <div
              v-if="currentTab === 'security'"
              class="card my-custom-card mb-4"
            >
              <div class="card-body">
                <div class="mb-4">
                  <h5 class="card-title">
                    비밀번호 변경
                  </h5>
                  <div class="mb-3">
                    <label class="form-label">현재 비밀번호</label>
                    <input
                      v-model="basicInfo.currentPassword"
                      type="password"
                      class="form-control"
                    >
                  </div>
                  <div class="mb-3">
                    <label class="form-label">새 비밀번호</label>
                    <input
                      v-model="basicInfo.newPassword"
                      type="password"
                      class="form-control"
                    >
                  </div>
                  <div class="mb-3">
                    <label class="form-label">새 비밀번호 확인</label>
                    <input
                      v-model="basicInfo.confirmPassword"
                      type="password"
                      class="form-control"
                    >
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 하단 버튼 -->
          <div class="d-flex justify-content-end my-4">
            <button
              class="btn btn-secondary me-2"
              @click="cancel"
            >
              취소
            </button>
            <button
              class="btn btn-primary"
              @click="save"
            >
              저장하기
            </button>
          </div>

          <!-- 알림 모달 (부트스트랩 모달 마크업) -->
          <div
            v-if="showModal"
            class="modal fade show d-block"
            tabindex="-1"
            style="background: rgba(0, 0, 0, 0.5);"
          >
            <div class="modal-dialog">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">
                    {{ modalTitle }}
                  </h5>
                  <button
                    type="button"
                    class="btn-close"
                    @click="closeModal"
                  />
                </div>
                <div class="modal-body">
                  <p>{{ modalMessage }}</p>
                </div>
                <div class="modal-footer">
                  <button
                    type="button"
                    class="btn btn-primary"
                    @click="closeModal"
                  >
                    확인
                  </button>
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
.my-custom-card {
  border: 0 !important;
  border-radius: 0 !important;
  box-shadow: 0 4px 5px -1px rgba(0, 0, 0, 0.5)
}
</style>
