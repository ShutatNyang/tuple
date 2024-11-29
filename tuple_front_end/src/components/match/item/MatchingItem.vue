<template>
  <div
    class="plan-item border p-4 rounded-md shadow-md bg-white flex items-start cursor-pointer"
    @click="goToDetail()"
  >
    <div class="flex-1 pr-60">
      <!-- 플랜 제목 -->
      <h2 class="text-lg font-bold mb-2">
        {{ plan.planTitle || "플랜 제목" }}
      </h2>

      <!-- 플랜 세부 정보 -->
      <p class="text-sm text-gray-700">
        <strong>여행지:</strong> {{ plan.startLocation }}
      </p>
      <p class="text-sm text-gray-700">
        <strong>기간:</strong> {{ plan.startDate }} ~ {{ plan.endDate }}
      </p>

      <!-- 차량 정보 -->
      <p v-if="plan.vehicle" class="text-sm text-gray-700">
        <strong>차량:</strong> {{ plan.vehicle }}
      </p>
      <p v-if="plan.toTourVehicle" class="text-sm text-gray-700">
        <strong>관광 차량:</strong> {{ plan.toTourVehicle }}
      </p>
      <p v-if="plan.plan" class="text-sm text-gray-700">
        <strong>작성자:</strong> {{ plan.email }}
      </p>
    </div>
    <!-- 이미지 블록 -->
    <div
      class="flex-1 bg-center bg-no-repeat aspect-video bg-cover rounded-xl"
      :style="{
        backgroundImage: `url(${thumbnailImage || profilePatrick})`,
      }"
    ></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      thumbnailImage: null,
    };
  },
  name: "TourPlanItem",
  props: {
    plan: {
      type: Object,
      required: true,
    },
  },
  mounted() {
    this.extractFirstImage();
  },
  methods: {
    goToDetail() {
      this.$router.push(`/match/detail/${this.plan.id}`);
    },

    extractFirstImage() {
      const plan = JSON.parse(this.plan.plan);
      for (const day of plan.days) {
        if (day.locations && day.locations.length > 0) {
          const firstLocation = day.locations[0];
          if (firstLocation.image) {
            this.thumbnailImage = firstLocation.image;
            break;
          }
        }
      }
    },
  },
};
</script>

<style scoped>
.plan-item {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.plan-item:hover {
  transform: translateY(-4px); /* 호버 시 살짝 위로 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1); /* 호버 시 그림자 효과 */
}
</style>
