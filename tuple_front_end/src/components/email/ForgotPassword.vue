<script setup>
import axios from "axios";
import { ref } from "vue";

// 이메일과 플래그 변수 선언
const email = ref("");
const isEmailValid = ref(false); // 이메일 유효 여부 플래그

// 이메일 전송 함수
async function submitEmail() {
  if (!email.value) {
    alert("이메일을 입력해주세요.");
    return;
  }

  if (!isEmailValid.value) {
    alert("이메일 확인을 먼저 진행해주세요.");
    return;
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email.value)) {
    alert("유효한 이메일 주소를 입력해주세요.");
    return;
  }

  try {
    const response = await axios.post(
      "http://localhost/email/send-reset-password",
      null,
      {
        params: { email: email.value },
      }
    );

    alert("비밀번호 재설정 이메일이 전송되었습니다.");
  } catch (error) {
    console.error("Error:", error.response || error);
    alert(
      error.response?.data?.message || "오류가 발생했습니다. 다시 시도해주세요."
    );
  }
}

// 이메일 확인 함수
async function verifyEmail() {
  if (!email.value) {
    alert("이메일을 입력해주세요.");
    isEmailValid.value = false; // 이메일 미입력 시 유효 여부를 리셋
    return;
  }

  try {
    const response = await axios.get(
      `http://localhost/member/idCheck/${email.value}`
    );
    if (response.status === 200 && response.data.bool) {
      alert("이메일이 확인되었습니다.");
      isEmailValid.value = true; // 이메일이 유효함
    } else {
      alert("해당 이메일이 존재하지 않습니다.");
      isEmailValid.value = false; // 이메일이 유효하지 않음
    }
  } catch (error) {
    console.error("Error:", error.response || error);
    alert(
      error.response?.data?.message || "이메일 확인 중 문제가 발생했습니다."
    );
    isEmailValid.value = false; // 오류 시 이메일 유효 여부 초기화
  }
}
</script>

<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-[#fcf8f9] group/design-root overflow-x-hidden"
    style="font-family: Manrope, 'Noto Sans', sans-serif"
  >
    <div class="layout-container flex h-full grow flex-col">
      <header
        class="flex items-center justify-between whitespace-nowrap border-solid border-b-[#f4e6e9] px-10 py-3"
      ></header>
      <div class="px-40 flex flex-1 justify-center py-5">
        <div
          class="layout-content-container flex flex-col w-[512px] max-w-[512px] py-5 max-w-[960px] flex-1 bg-white"
        >
          <div class="flex flex-wrap justify-between gap-3 p-4">
            <div class="flex min-w-72 flex-col gap-3">
              <p
                class="text-[#1c0d10] tracking-light text-[32px] font-bold leading-tight"
              >
                비밀번호 재설정
              </p>
              <p class="text-[#9e4759] text-sm font-normal leading-normal">
                이메일을 입력해주세요
              </p>
            </div>
          </div>
          <div class="flex max-w-[520px] flex-wrap gap-4 px-4 py-3">
            <!-- 이메일 입력창 -->
            <label class="flex-grow">
              <input
                placeholder="Enter your email"
                class="form-input w-full resize-none overflow-hidden rounded-l-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border border-[#e9ced3] bg-[#fcf8f9] focus:border-[#e9ced3] h-14 placeholder:text-[#9e4759] p-[15px] text-base font-normal leading-normal"
                v-model="email"
                type="email"
              />
            </label>
            <!-- 이메일 확인 버튼 -->
            <button
              class="w-28 flex-shrink-0 items-center justify-center rounded-r-xl h-14 bg-[#fc6986] text-[#fcf8f9] text-sm font-bold leading-normal tracking-[0.015em]"
              @click="verifyEmail"
            >
              이메일 확인
            </button>
          </div>
          <div class="flex px-4 py-3">
            <button
              class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 flex-1 bg-[#fc6986] text-[#fcf8f9] text-sm font-bold leading-normal tracking-[0.015em]"
              @click="submitEmail"
            >
              <span class="truncate">Submit</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
