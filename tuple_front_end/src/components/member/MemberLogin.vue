<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden"
    :style="{
      '--checkbox-tick-svg': checkboxTickSvg,
      'font-family': 'Plus Jakarta Sans, Noto Sans, sans-serif',
    }"
  >
    <MemberHeader />
    <div class="layout-container flex h-full grow flex-col">
      <div class="px-40 flex flex-1 justify-center py-5">
        <div
          class="layout-content-container flex flex-col w-[512px] max-w-[512px] py-5 max-w-[960px] flex-1"
        >
          <h2
            class="text-[#181112] text-lg font-bold leading-tight tracking-[-0.015em] px-4 text-left pb-2 pt-4"
          >
            Login
          </h2>
          <form @submit.prevent="login">
            <div class="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
              <label class="flex flex-col min-w-40 flex-1">
                <p
                  class="text-[#181112] text-base font-medium leading-normal pb-2"
                >
                  ID
                </p>
                <input
                  v-model="email"
                  type="text"
                  class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white focus:border-[#e6dbdd] h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal leading-normal"
                />
              </label>
            </div>
            <div class="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
              <label class="flex flex-col min-w-40 flex-1">
                <p
                  class="text-[#181112] text-base font-medium leading-normal pb-2"
                >
                  PASSWORD
                </p>
                <input
                  v-model="password"
                  type="password"
                  class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white focus:border-[#e6dbdd] h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal leading-normal"
                />
              </label>
            </div>
            <div class="px-4">
              <label class="flex gap-x-3 py-3 flex-row">
                <input
                  type="checkbox"
                  v-model="keepLoggedIn"
                  class="h-5 w-5 rounded border-[#e6dbdd] border-2 bg-transparent text-[#fc6986] checked:bg-[#fc6986] checked:border-[#fc6986] focus:ring-0 focus:outline-none"
                />
                <p class="text-[#181112] text-base font-normal leading-normal">
                  Keep me logged in
                </p>
              </label>
              <p
                class="text-[#8c5f68] text-sm font-normal leading-normal pb-3 pt-1 px-4 text-center underline cursor-pointer"
                @click="goForgotEmail"
              >
                이메일을 잊어버리셨나요?
              </p>
              <p
                class="text-[#8c5f68] text-sm font-normal leading-normal pb-3 pt-1 px-4 text-center underline cursor-pointer"
                @click="goForgotPassword"
              >
                비밀번호를 잊어버리셨나요?
              </p>
            </div>
            <div class="flex px-4 py-3">
              <button
                type="submit"
                class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 flex-1 bg-[#fc6986] text-white text-sm font-bold leading-normal tracking-[0.015em]"
              >
                <span class="truncate">Log in</span>
              </button>
            </div>
            <p
              class="text-[#8c5f68] text-sm font-normal leading-normal pb-3 pt-1 px-4 text-center"
            >
              or
            </p>
            <div class="flex px-4 py-3">
              <button
                class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 flex-1 bg-[#f5f0f1] text-[#181112] gap-2 pl-4 text-sm font-bold leading-normal tracking-[0.015em]"
              >
                <div class="text-[#181112]">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="20px"
                    height="20px"
                    fill="currentColor"
                    viewBox="0 0 256 256"
                  >
                    <path
                      d="M224,128a96,96,0,1,1-21.95-61.09,8,8,0,1,1-12.33,10.18A80,80,0,1,0,207.6,136H128a8,8,0,0,1,0-16h88A8,8,0,0,1,224,128Z"
                    ></path>
                  </svg>
                </div>
                <span class="truncate">Continue with Kakao</span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from "@/stores/auth";
import axios from "axios";
import MemberHeader from "./MemberHeader.vue";

export default {
  components: {
    MemberHeader,
  },
  data() {
    return {
      email: "",
      password: "",
      keepLoggedIn: false,
      checkboxTickSvg: `url('data:image/svg+xml,%3csvg viewBox=%270 0 16 16%27 fill=%27rgb(255,255,255)%27 xmlns=%27http://www.w3.org/2000/svg%27%3e%3cpath d=%27M12.207 4.793a1 1 0 010 1.414l-5 5a1 1 0 01-1.414 0l-2-2a1 1 0 011.414-1.414L6.5 9.086l4.293-4.293a1 1 0 011.414 0z%27/%3e%3c/svg%3e')`,
    };
  },
  methods: {
    goForgotPassword() {
      this.$router.push("/forgot-password");
    },
    goForgotEmail() {
      this.$router.push("/forgot-email");
    },
    login() {
      const loginData = {
        email: this.email,
        password: this.password,
        remember: this.keepLoggedIn ? "on" : "off",
      };

      axios
        .post("http://localhost/member/login", new URLSearchParams(loginData), {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
        })
        .then((response) => {
          if (response.status === 200) {
            const authStore = useAuthStore();
            authStore.setLoginUser(
              response.data.access_token,
              response.data.refresh_token
            );

            this.$router.push("/");
          }
        })
        .catch((error) => {
          console.error("Login Failed:", error);

          // 서버에서 반환된 응답이 있을 경우
          if (error.response) {
            const status = error.response.status;
            const data = error.response.data;

            // HTTP 상태 코드에 따라 예외 처리
            if (status === 403 && data.error === "NotAuthorized") {
              alert("이메일 인증이 필요합니다. 이메일을 확인해주세요.");
            } else if (status === 401) {
              alert("로그인 실패: 아이디와 비밀번호를 확인하세요.");
            } else {
              alert("알 수 없는 오류가 발생했습니다. 관리자에게 문의하세요.");
            }
          } else {
            // 서버 응답이 없거나 네트워크 오류
            alert("서버와 통신할 수 없습니다. 네트워크를 확인하세요.");
          }
        });
    },
  },
};
</script>

<style scoped>
/* 추가적인 스타일은 필요에 따라 정의합니다. */
</style>
