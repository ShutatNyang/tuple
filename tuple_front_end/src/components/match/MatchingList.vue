<template>
  <div class="px-10 flex flex-1 justify-center py-5">
    <div class="layout-content-container flex flex-col max-w-[960px] flex-1">
      <h2 class="text-lg font-semibold mb-4">여행어때</h2>
      <ul>
        <li v-for="plan in requestPlans" :key="plan.id" class="mb-4">
          <MatchingItem :plan="plan" />
        </li>
      </ul>
      <!-- 페이지 네비게이션 추가 시 필요 -->
      <!-- <PageNavigator :total="totalRequests" :v-model:propName="currentPage" /> -->
    </div>
  </div>
</template>

<script>
import MatchingItem from "@/components/match/item/MatchingItem.vue";
import PageNavigator from "@/components/match/PageNavigator.vue";
import axios from "axios";

export default {
  name: "MatchingList",
  components: {
    MatchingItem,
    PageNavigator,
  },
  props: {
    myId: {
      type: Number,
      required: true, // 현재 사용자 ID
    },
  },
  data() {
    return {
      requestPlans: [], // 최종적으로 렌더링할 여행 계획 데이터
      tourMatchIds: [], // 매칭된 tour_plan_id 목록
    };
  },
  async created() {
    // 컴포넌트가 생성되었을 때 호출
    await this.fetchTourMatchIds(); // tour_match 테이블 데이터 호출
    await this.fetchFilteredTourPlans(); // 필터링된 여행 계획 데이터 호출
  },
  methods: {
    // 1. tour_match 테이블에서 데이터 가져오기
    async fetchTourMatchIds() {
      try {
        const response = await axios.post(
          "http://localhost/tour-match/list/await",
          null,
          {
            params: {
              memberId: this.myId, // 현재 사용자 제외
            },
          }
        );
        // 가져온 데이터에서 tour_plan_id만 추출
        this.tourMatchIds = response.data
          .filter((match) => match.status === "pending")
          .map((match) => match.tourPlanId);
      } catch (error) {
        console.error("매칭 데이터를 불러오는 중 오류 발생:", error);
        alert("매칭 데이터를 불러오는 데 실패했습니다.");
      }
    },

    // 2. 모든 여행 계획 가져오기 후 필터링
    async fetchFilteredTourPlans() {
      try {
        const response = await axios.post(
          "http://localhost/tour-plan/list/all"
        ); // 모든 여행 계획 가져오기
        const allPlans = response.data;

        // tour_plan_id와 매칭된 데이터만 필터링
        this.requestPlans = allPlans.filter((plan) =>
          this.tourMatchIds.includes(plan.id)
        );
      } catch (error) {
        console.error("여행 계획 데이터를 불러오는 중 오류 발생:", error);
        alert("여행 계획 데이터를 불러오는 데 실패했습니다.");
      }
    },
  },
};
</script>

<style scoped>
.layout-content-container {
  display: flex;
  flex-direction: column;
  max-width: 960px;
  flex: 1;
  margin: 0 auto;
}
</style>
