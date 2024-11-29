<template>
  <button
    class="make-plan-button px-6 py-2 text-white font-bold rounded-md shadow-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-300 transition"
    @click="navigateToMakePlan"
  >
    여행 계획 작성
  </button>
</template>

<script>
export default {
  name: "MakePlanButton",
  methods: {
    navigateToMakePlan() {

      this.$router.push("/tour-plan/write-plan");
    },
  },
};
</script>

<style scoped>
.make-plan-button {
  font-size: 1rem; /* 글씨 크기 적당히 키움 (text-base) */
  line-height: 1.25rem;
  padding: 0.5rem 1.5rem; /* 상하 0.5rem, 좌우 1.5rem */
  cursor: pointer;
  transition: background-color 0.2s ease, transform 0.2s ease;
  background-color: rgb(252 105 134);
}

.make-plan-button:hover {
  transform: translateY(-2px); /* 호버 시 살짝 위로 이동 */
}
</style>
