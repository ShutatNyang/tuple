<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-[#fcf8f9] group/design-root overflow-x-hidden"
    style="font-family: Manrope, 'Noto Sans', sans-serif"
  >
    <div class="layout-container flex h-full grow flex-col">
      <header
        class="flex items-center justify-between whitespace-nowrap border-solid border-b-[#f4e6e9] px-10 py-3"
      >
        <div class="flex items-center gap-4 text-[#1c0d10]">
          <div class="size-4"></div>
          <h2
            class="text-[#1c0d10] text-lg font-bold leading-tight tracking-[-0.015em]"
          ></h2>
        </div>
      </header>
      <div class="px-40 flex flex-1 justify-center py-5">
        <div
          class="layout-content-container flex flex-col w-[512px] max-w-[512px] py-5 max-w-[960px] flex-1 bg-white"
        >
          <div class="flex flex-wrap justify-between gap-3 p-4">
            <div class="flex min-w-72 flex-col gap-3">
              <p
                class="text-[#1c0d10] tracking-light text-[32px] font-bold leading-tight"
              >
                Create a password
              </p>
              <p class="text-[#9e4759] text-sm font-normal leading-normal">
                Your password must be at least 8 characters long and contain at
                least one letter and one number
              </p>
            </div>
          </div>
          <div class="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
            <label class="flex flex-col min-w-40 flex-1">
              <p
                class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
              >
                Password
              </p>
              <input
                placeholder="Enter your password"
                class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border border-[#e9ced3] bg-[#fcf8f9] focus:border-[#e9ced3] h-14 placeholder:text-[#9e4759] p-[15px] text-base font-normal leading-normal"
                v-model="password"
                type="password"
              />
            </label>
          </div>
          <div class="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
            <label class="flex flex-col min-w-40 flex-1">
              <p
                class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
              >
                Confirm password
              </p>
              <input
                placeholder="Re-enter your password"
                class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border border-[#e9ced3] bg-[#fcf8f9] focus:border-[#e9ced3] h-14 placeholder:text-[#9e4759] p-[15px] text-base font-normal leading-normal"
                v-model="confirmPassword"
                type="password"
              />
            </label>
          </div>
          <div class="flex px-4 py-3">
            <button
              class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 flex-1 bg-[#fc6986] text-[#fcf8f9] text-sm font-bold leading-normal tracking-[0.015em]"
              @click="resetPassword"
            >
              <span class="truncate">비밀번호 변경</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { useRoute } from "vue-router";

export default {
  setup() {
    const route = useRoute();
    return { route };
  },
  data() {
    return {
      password: "",
      confirmPassword: "",
    };
  },
  methods: {
    async resetPassword() {
      const token = this.route.query.token; // URL에서 토큰 가져오기

      if (!token) {
        alert("유효하지 않은 요청입니다.");
        return;
      }

      if (this.password !== this.confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
      }

      try {
        const response = await axios.post(
          "http://localhost/email/reset-password",
          {},
          {
            params: {
              token,
              newPassword: this.password,
            },
          }
        );

        if (response.status === 200) {
          alert(response.data.message || "비밀번호 변경이 완료되었습니다.");
          location.href = "http://localhost:5173/";
        } else {
          alert(response.data.message || "비밀번호 변경에 실패했습니다.");
        }
      } catch (error) {
        console.error("Error:", error.response || error);
        alert(
          error.response?.data?.message ||
            "오류가 발생했습니다. 다시 시도해주세요."
        );
      }
    },
  },
};
</script>

<style scoped></style>
