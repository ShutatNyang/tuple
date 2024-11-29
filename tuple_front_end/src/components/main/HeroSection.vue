<template>
  <div
    class="min-h-[480px] flex flex-col gap-6 bg-cover bg-center bg-no-repeat items-start justify-end px-4 pb-10"
    :style="{
      backgroundImage:
        'linear-gradient(rgba(0, 0, 0, 0.1) 0%, rgba(0, 0, 0, 0.4) 100%), url(/image/main.jpg)',
      borderRadius: '20px 20px 0 0' /* 위쪽 양옆만 둥글게 설정 */,
    }"
  >
    <div class="flex flex-col gap-2 text-left">
      <h1
        class="text-white text-4xl font-black leading-tight tracking-[-0.033em]"
      >
        tuple
      </h1>
      <h2 class="text-white text-sm font-normal leading-normal">
        투플에서 계획을 세우고 함께 떠나세요
      </h2>
    </div>
    <button
      class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#ff9dbe] text-[#181013] text-sm font-bold leading-normal tracking-[0.015em]"
      @click="goWrite"
    >
      지금 시작하기→
    </button>
  </div>
</template>

<script>
export default {
  methods: {
    goWrite() {
      this.$router.push("/tour-plan/write-plan");
    },
  },
};
</script>

<style></style>
