<template>
  <div
    class="plan-item border p-4 rounded-md shadow-md bg-white"
    :class="{
      'pointer-events-none opacity-50 border p-4 rounded-md shadow-md bg-white':
        isDisabled,
    }"
    @click="handleClickItem"
  >
    <!-- 이미지 블록 -->
    <div
      class="flex-1 bg-center bg-no-repeat aspect-video bg-cover rounded"
      :style="{
        backgroundImage: `url(${thumbnailImage || profilePatrick})`,
      }"
    ></div>

    <!-- 플랜 제목 -->
    <h2 class="text-lg font-bold mb-2 pt-2">
      {{ plan.planTitle || "플랜 제목" }}
    </h2>

    <!-- 플랜 세부 정보 -->
    <p class="text-sm text-gray-700">
      <strong>여행지:</strong> {{ plan.startLocation }}
    </p>
    <p class="text-sm text-gray-700">
      <strong>기간:</strong> {{ plan.startDate }} ~ {{ plan.endDate }}
    </p>
  </div>

  <MatchModal
    v-if="openModal"
    @close="handleClickItem"
    @accepted="$emit('accepted')"
    :tourPlanId="plan.id"
    :memberId="myId"
  />
</template>

<script>
import profilePatrick from "@/components/icons/Profile_Patrick.png";
import MatchModal from "@/components/match/MatchModal.vue";

export default {
  components: {
    MatchModal,
  },
  data() {
    return {
      profilePatrick,
      openModal: false,
      thumbnailImage: null,
    };
  },
  name: "TourPlanItem",
  props: {
    plan: {
      type: Object,
      required: true,
    },
    myId: {
      type: Number,
      required: true,
    },
    status: {
      type: String,
      required: true,
    },
  },
  mounted() {
    this.extractFirstImage();
  },
  computed: {
    isDisabled() {
      return this.status === "matched" || this.status === "rejected";
    },
  },
  methods: {
    handleClickItem() {
      this.openModal = !this.openModal;
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
.pointer-events-none {
  pointer-events: none; /* 클릭 방지 */
}
.opacity-50 {
  opacity: 0.5; /* 비활성화 시 시각 효과 */
}
</style>
