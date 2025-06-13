<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from '@/axios'
import { useStore } from 'vuex'
import api from "@/axios";  // Vuex 사용 시

/* ---- Composition State ---- */

// 로그인 폼 vs 회원가입 폼 전환
const isSignInActive = ref(true)
// 모바일 뷰 vs 데스크톱 뷰 표시
const isMobileView = ref(false)

// 로그인 정보
const credentials = reactive({
  email: '',
  password: ''
})

// 회원가입 정보
const userInfo = reactive({
  email: '',
  name: '',
  password: '',
  phone: '',
  address: '',
  role: ''
})

/* ---- Vue Router & Vuex Store ---- */
const route = useRoute()
const router = useRouter()
const store = useStore()   // Vuex 스토어

/* ---- Lifecycle: mounted ---- */
onMounted(() => {
  // 라우트 이름에 따라 초기 폼상태 결정
  if (route.name === 'signup') {
    isSignInActive.value = false
  } else if (route.name === 'signin') {
    isSignInActive.value = true
  }
})

/* ---- Watch route changes ---- */
watch(
  () => route.name,
  (newName) => {
    if (newName === 'signup') {
      isSignInActive.value = false
    } else if (newName === 'signin') {
      isSignInActive.value = true
    }
  }
)

/* ---- Methods ---- */

// SignIn <-> SignUp 전환
function toggleForm() {
  isSignInActive.value = !isSignInActive.value
}

// 모바일/데스크톱 뷰 전환
function toggleView(isMobile) {
  isMobileView.value = isMobile
}

// 로그인
async function signIn() {
  try {
    const response = await axios.post('/members/signin', credentials, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    })
    const { token, username, role } = response.data

    // Vuex 스토어에 로그인 정보 저장
    await store.dispatch('login', {token, userInfo: {username, role}})

    // 로그인 성공 후 리다이렉트
    if (response.data.needAddress) {
      await router.push({
        path: "/seller/sellerModifyInfo",
        query: { showAlert: "true" },
      });
    } else {
      // 홈 화면 등으로 이동
      this.$router.push("/");
    }
  } catch (error) {
    if (error?.response?.status === 404) {
      alert('회원 정보가 존재하지 않습니다. ' + error)
    } else {
      alert('아이디 혹은 비밀번호가 맞지 않습니다. ' + error)
    }
    console.error('Login error', error)
  }
}

// 회원가입
async function signUp() {
  try {
    const response = await api.post('/members/signup', userInfo)
    // 가입 성공 시 메시지
    alert(response.data)
    // 폼 전환: 회원가입 -> 로그인
    toggleForm()
  } catch (error) {
    console.error('회원가입 error', error)
    alert('오류가 발생했습니다. ' + error.response.data.message)
  }
}
</script>

<template>
  <div
    :class="{ 'mobile-root': isMobileView }"
    class="root"
  >
    <!-- SignIn 폼 -->
    <div
      :class="{ 'active': isSignInActive, 'inactive': !isSignInActive }"
      class="signin-wrapper form"
    >
      <div class="form-wrapper">
        <h5>Welcome Back 👊</h5>
        <input
          id="email"
          v-model="credentials.email"
          class="form-field"
          placeholder="Email"
          type="text"
          @keyup.enter="signIn"
        >
        <input
          id="password"
          v-model="credentials.password"
          class="form-field"
          placeholder="Password"
          type="password"
          @keyup.enter="signIn"
        >
        <button
          class="button primary"
          @click="signIn"
        >
          Sign In
        </button>
        <button
          class="button secondary"
          @click="toggleForm"
        >
          Sign Up
        </button>
        <p>
          view concept for
          <a @click="toggleView(true)"><b>mobile</b></a>
          or for
          <a @click="toggleView(false)"><b>desktop</b></a>
        </p>
      </div>
    </div>

    <!-- SignUp 폼 -->
    <div
      :class="{ 'active': !isSignInActive, 'inactive': isSignInActive }"
      class="signup-wrapper form"
    >
      <div class="form-wrapper">
        <h5>👋 Hello</h5>
        <input
          id="email"
          v-model="userInfo.email"
          class="form-field"
          placeholder="Email"
          type="text"
        >
        <input
          id="name"
          v-model="userInfo.name"
          class="form-field"
          placeholder="UserName"
          type="text"
        >
        <input
          id="password"
          v-model="userInfo.password"
          class="form-field"
          placeholder="Password"
          type="password"
        >
        <input
          id="phone"
          v-model="userInfo.phone"
          class="form-field"
          placeholder="phone"
          type="text"
        >
        <div class="form-field">
          <label class="radio-label">
            <input
              v-model="userInfo.role"
              name="role"
              type="radio"
              value="USER"
            >
            구매자
          </label>
          <label class="radio-label">
            <input
              v-model="userInfo.role"
              name="role"
              type="radio"
              value="SELLER"
            >
            판매자
          </label>
        </div>
        <button
          class="button primary"
          @click="signUp"
        >
          Sign Up
        </button>
        <button
          class="button secondary"
          @click="toggleForm"
        >
          Sign In
        </button>

      </div>
    </div>
  </div>
</template>

<style scoped>
.root {
  display: flex;
  width: 100%;
  height: 100vh;
}
.mobile-root {
  flex-direction: column;
}
.mobile-root .inactive {
  display: none;
}
.signin-wrapper {
  flex-grow: 1;
  background: #d5d5d5;
  transition: all 0.32s ease-in;
}
.signup-wrapper {
  flex-grow: 1;
  background: #0F3758;
  transition: all 0.32s ease-in-out;
}
.active {
  flex-grow: 6;
}
.active.form {
  z-index: 5;
}
.inactive {
  pointer-events: none;
  filter: blur(2px) grayscale(80%);
}
.inactive .form-wrapper {
  filter: opacity(55%);
}
html, body {
  font-family: 'Roboto';
  height: 100vh;
  padding: 0;
  margin: 0;
}
h5 {
  color: #212121;
  font-size: 20px;
  margin: 15px 0 30px 0;
  text-align: center;
}
.form,
.form-wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.form-wrapper {
  background-color: white;
  width: 300px;
  height: 500px;
  padding: 20px 0;
  margin: 20px auto;
  border-radius: 15px;
}
.form-wrapper button {
  cursor: pointer;
  font-weight: bold;
  width: 230px;
  margin: 0 35px 20px;
  height: 45px;
  padding: 6px 15px;
  border-radius: 5px;
  outline: none;
  border: none;
  font-size: 14px;
}
.form-wrapper button.primary {
  color: white;
  background: #0F3758;
}
.form-wrapper button.primary:hover {
  opacity: 0.9;
}
.form-wrapper button.secondary {
  background: white;
  color: #0F3758;
}
.form-wrapper button.secondary:hover {
  background: #f5f5f5;
}
.form-wrapper .form-field {
  font-weight: bold;
  width: 200px;
  margin: 0 35px 20px;
  height: 35px;
  padding: 6px 15px;
  border-radius: 5px;
  outline: none;
  border: none;
  background: #f5f5f5;
  color: #748194;
  font-size: 14px;
}
.form-wrapper p {
  color: #424242;
  font-size: 14px;
  text-align: center;
}
.form-wrapper p a {
  cursor: pointer;
  color: #0F3758;
}
.form-wrapper p a:hover {
  color: #009688;
}
.radio-label {
  margin-right: 20px;
  font-weight: bold;
}
</style>
