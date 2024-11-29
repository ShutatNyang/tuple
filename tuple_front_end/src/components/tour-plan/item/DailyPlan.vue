<template>
  <div class="daily-plan">
    <h2>날짜: {{ plan.date }}</h2>
    <ul>
      <li v-for="(location, index) in plan.locations" :key="index">
        장소: {{ location.name }} ({{ location.memo }})
      </li>
    </ul>
    <p v-if="plan.accommodation">
      숙소: {{ plan.accommodation.name }} ({{ plan.accommodation.memo }})
    </p>
    <p v-else>숙소: 없음</p>
  </div>
</template>
<script>
export default {
  props: {
    plan: {
      type: Object,
      required: true, // 부모로부터 반드시 전달받아야 함
    },
  },

};
</script>
<style scoped>
.daily-plan {
  border: 1px solid #ccc;
  padding: 16px;
  margin: 8px 0;
  border-radius: 8px;
}
</style>
