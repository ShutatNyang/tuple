<template>
  <div>
    <!-- 플랜이 없을 경우 -->
    <p v-if="displayedPlans.length === 0" class="text-gray-500">아직 계획이 없어요.</p>

    <!-- 플랜이 있을 경우 -->
    <ul v-else>
      <!-- TourPlanItem 호출 -->
      <li
        v-for="plan in displayedPlans"
        :key="plan.id"
        @click="navigateToDetail(plan.id)"
        class="mb-4 cursor-pointer"
      >
        <TourPlanItem :plan="plan" />
      </li>
    </ul>
  </div>
</template>

<script>
import TourPlanItem from "./item/TourPlanItem.vue";

export default {
  name: "PlanList",
  components: {
    TourPlanItem, // 하위 컴포넌트 등록
  },
  props: {
    plans: {
      type: Array,
      required: true, // 부모로부터 반드시 전달받아야 함
    },
    showAll: {
      type: Boolean,
      default: false,
    },
    myId: {
      type: String,
      required: true,
    },
  },
  computed: {
    displayedPlans() {
      return this.showAll
        ? this.plans
        : this.plans.filter((plan) => plan.ownerId === this.myId);
    },
  },
  methods: {
    navigateToDetail(id) {
      this.$router.push(`/tour-plan/detail/${id}`);
    },
  },
};
</script>

<style scoped>
/* 필요하면 스타일 추가 */
</style>
