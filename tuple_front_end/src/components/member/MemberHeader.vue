<!-- src/components/Header.vue -->
<template>
  <header
    class="flex items-center justify-between whitespace-nowrap border-b border-solid border-b-[#f5f0f1] px-10 py-3"
  >
    <div class="flex items-center gap-4 text-[#181112]">
      <div class="size-4">
        <!-- 로고 SVG -->
        <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path
            fill-rule="evenodd"
            clip-rule="evenodd"
            d="M12.0799 24L4 19.2479L9.95537 8.75216L18.04 13.4961L18.0446 4H29.9554L29.96 13.4961L38.0446 8.75216L44 19.2479L35.92 24L44 28.7521L38.0446 39.2479L29.96 34.5039L29.9554 44H18.0446L18.04 34.5039L9.95537 39.2479L4 28.7521L12.0799 24Z"
            fill="currentColor"
          ></path>
        </svg>
      </div>
      <h2
        class="text-[#181112] text-lg font-bold leading-tight tracking-[-0.015em]"
      >
        Welcome to tuple
      </h2>
    </div>
  </header>
</template>

<script>
export default {
  name: "Header",
};
</script>

<style scoped>
/* 헤더 관련 스타일이 필요하면 추가 */
</style>
