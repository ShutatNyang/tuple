<script setup>
import axios from "axios";
import { ref } from 'vue';

const name = ref('');
const phone = ref('');
const resultMessage = ref(''); // 결과 메시지

async function submitForm() {
  if (!name.value) {
    alert('이름을 입력해주세요.');
    return;
  }
  if (!phone.value) {
    alert('전화번호를 입력해주세요.');
    return;
  }
  const phoneRegex = /^[0-9]{10,11}$/;
  if (!phoneRegex.test(phone.value)) {
    alert('유효한 전화번호를 입력해주세요. (10~11자리 숫자)');
    return;
  }

  try {
    const response = await axios.post('http://localhost/member/find-email', null, {
      params: { name: name.value, phone: phone.value },
    });

    if (response.status === 200 && response.data) {
      resultMessage.value = `해당 이메일은: ${response.data}입니다.`;
    } else {
      resultMessage.value = '입력한 정보로 이메일을 찾을 수 없습니다.';
    }
  } catch (error) {
    console.error('Error:', error.response || error);
    resultMessage.value = '이메일 찾기 중 문제가 발생했습니다.';
  }
}
</script>

<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-[#fcf8f9] group/design-root overflow-x-hidden"
    style="font-family: Manrope, 'Noto Sans', sans-serif;"
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
                아이디(이메일) 찾기
              </p>
              <p
                class="text-[#9e4759] text-sm font-normal leading-normal"
              >
                가입 시 입력한 이름과 전화번호를 입력해주세요.
              </p>
            </div>
          </div>
          <!-- 이름 입력 -->
          <div class="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
            <label class="flex flex-col min-w-40 flex-1">
              <p
                class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
              >
                이름
              </p>
              <input
                placeholder="이름을 입력하세요"
                class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border border-[#e9ced3] bg-[#fcf8f9] focus:border-[#e9ced3] h-14 placeholder:text-[#9e4759] p-[15px] text-base font-normal leading-normal"
                v-model="name"
                type="text"
              />
            </label>
          </div>
          <!-- 전화번호 입력 -->
          <div class="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
            <label class="flex flex-col min-w-40 flex-1">
              <p
                class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
              >
                전화번호
              </p>
              <input
                placeholder="전화번호를 입력하세요 (숫자만 입력)"
                class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border border-[#e9ced3] bg-[#fcf8f9] focus:border-[#e9ced3] h-14 placeholder:text-[#9e4759] p-[15px] text-base font-normal leading-normal"
                v-model="phone"
                type="tel"
              />
            </label>
          </div>
          <!-- 결과 메시지 -->
          <div class="px-4 py-2">
            <p class="text-blue-500 text-sm font-medium leading-normal">
              {{ resultMessage }}
            </p>
          </div>
          <!-- 제출 버튼 -->
          <div class="flex px-4 py-3">
            <button
              class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 flex-1 bg-[#fc6986] text-[#fcf8f9] text-sm font-bold leading-normal tracking-[0.015em]"
              @click="submitForm"
            >
              <span class="truncate">제출</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>
