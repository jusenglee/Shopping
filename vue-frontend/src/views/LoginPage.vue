<template>
  <div :class="{ 'mobile-root': isMobileView }" class="root">
    <div :class="{ 'active': isSignInActive, 'inactive': !isSignInActive }"
         class="signin-wrapper form">
      <div class="form-wrapper">
        <h5>Welcome Back 👊</h5>
        <input id="email" v-model="credentials.email" class="form-field" placeholder="Email" type="text"
               @keyup.enter="signIn"/>
        <input id="password" v-model="credentials.password" class="form-field" placeholder="Password" type="password"
               @keyup.enter="signIn"/>
        <button class="button primary" @click="signIn">Sign In</button>
        <button class="button secondary" @click="toggleForm">Sign Up</button>
        <p>
          view concept for
          <a @click="toggleView(true)"><b>mobile</b></a>
          or for
          <a @click="toggleView(false)"><b>desktop</b></a>
        </p>
      </div>
    </div>
    <div :class="{ active: !isSignInActive , inactive: isSignInActive }"
         class="signup-wrapper form">
      <div class="form-wrapper">
        <h5>👋 Hello</h5>
        <input id="email" v-model="userInfo.email" class="form-field" placeholder="Email" type="text"/>
        <input id="name" v-model="userInfo.name" class="form-field" placeholder="UserName" type="text"/>
        <input id="password" v-model="userInfo.password" class="form-field" placeholder="Password" type="password"/>
        <input id="address" v-model="userInfo.address" class="form-field" placeholder="address" type="text"/>
        <input id="phone" v-model="userInfo.phone" class="form-field" placeholder="phone" type="text"/>
        <div class="form-field">
          <label class="radio-label"><input v-model="userInfo.role" name="role" type="radio" value="ROLE_USER">
            구매자</label>
          <label class="radio-label"><input v-model="userInfo.role" name="role" type="radio" value="ROLE_ADMIN">
            판매자</label>
        </div>
        <button class="button primary" @click="signUp">Sign Up</button>
        <button class="button secondary" @click="toggleForm">Sign In</button>
        <p>
          view concept for
          <a @click="toggleView(true)"><b>mobile</b></a>
          or for
          <a @click="toggleView(false)"><b>desktop</b></a>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "@/axios";

export default {
  data() {
    return {
      userInfo: {
        email: '',
        name: '',
        password: '',
        phone: '',
        address: '',
        role: ''
      },
      credentials: {
        email: '',
        password: ''
      },
      isSignInActive: true,
      isMobileView: false,
    };
  },
  mounted() {
    if (this.$route.name === 'signup') {
      this.isSignInActive = false;
    }
    if (this.$route.name === 'signin') {
      this.isSignInActive = true;
    }
  },
  watch: {
    '$route'(to) {
      if (to.name === 'signup') {
        this.isSignInActive = false;
      }
      if (to.name === 'signin') {
        this.isSignInActive = true;
      }
    }
  },
  methods: {
    toggleForm() {
      this.isSignInActive = !this.isSignInActive;
    },
    toggleView(isMobile) {
      this.isMobileView = isMobile;
    },
    signIn() {
      axios({
        method: 'post',
        url: '/members/signin', // 스프링 시큐리티 로그인 URL
        data: this.credentials,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      }).then(response => {
        const {token, username, role} = response.data;
        this.$store.dispatch('login', {token, userInfo: {username, role}});
        // 로그인 성공 후의 처리, 예를 들어 메인 페이지로 리다이렉트
        this.$router.push('/');
      })
          .catch(error => {
            // 로그인 실패 시의 처리 로직
            console.error('Login error', error);
            alert('아이디 혹은 비밀번호가 맞지않습니다.')
          });
    },
    signUp() {
      axios({
        method: 'post',
        url: '/members/signup', // 스프링 시큐리티 회원가입 URL
        data: this.userInfo,
      }).then(response => {
        alert(response.data, this.toggleForm());
      }).catch(error => {
        // 로그인 실패 시의 처리 로직
        console.error('회원가입 error', error);
      });
    },
  },
};
</script>

<style>
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
  transition: all .32s ease-in;
}

.signup-wrapper {
  flex-grow: 1;
  background: #0F3758;
  transition: all .32s ease-in-out;
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

.form, .form-wrapper {
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
  opacity: .9;
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
  margin-right: 20px; /* 라디오 버튼 간의 간격 */
  font-weight: bold;
  /* 추가적인 스타일링 (색상, 폰트 등) */
}
</style>
