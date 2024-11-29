<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden"
    :style="{ fontFamily: 'Plus Jakarta Sans, Noto Sans, sans-serif' }"
  >
    <div class="layout-container flex h-full grow flex-col">
      <div class="flex flex-1 justify-center items-center py-5" id="pink">
        <div
          class="layout-content-container flex flex-col w-full max-w-[960px] flex-1"
          id="white"
        >
          <!-- 탭: 특정 라우트에서만 표시 -->
          <TourPlanTab v-if="shouldShowTabs" />

          <!-- 로딩 상태 -->
          <div v-if="loading" class="flex justify-center items-center mt-10">
            <svg
              class="animate-spin h-8 w-8 text-gray-600"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
            >
              <circle
                class="opacity-25"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                stroke-width="4"
              ></circle>
              <path
                class="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8v8H4z"
              ></path>
            </svg>
          </div>

          <!-- 자식 라우트가 렌더링될 위치 -->
          <router-view
            :plans="showAll ? plans : myPlans"
            :show-all="showAll"
            :my-id="myId"
          ></router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import TourPlanTab from "../tour-plan/TourPlanTab.vue";

export default {
  name: "TourPlanView",
  components: {
    TourPlanTab,
  },
  data() {
    return {
      loading: false,
      error: null,
      plans: [], // 전체 플랜 데이터를 저장
      myPlans: [], // 필터링된 플랜 (내 플랜)
      accessToken: sessionStorage.getItem("accessToken"), // 토큰 가져오기
      myEmail: "", // 내 이메일
      myId: "", // 내 ID
      showAll: false, // 전체 플랜 표시 여부
    };
  },
  computed: {
    // 현재 라우트 이름을 기반으로 탭 표시 여부 결정
    shouldShowTabs() {
      const excludedRoutes = ["TourPlanWrite", "TourPlanDetail"];
      return !excludedRoutes.includes(this.$route.name);
    },
    // 현재 라우트 이름을 기반으로 계획 작성 버튼 표시 여부 결정
    shouldShowMakePlanButton() {
      const excludedRoutes = ["TourPlanWrite", "TourPlanDetail"];
      return !excludedRoutes.includes(this.$route.name);
    },
  },
  methods: {
    async fetchData() {
      this.loading = true;
      this.error = null;

      try {
        // 전체 플랜 데이터 가져오기
        const response = await axios.post(
          "http://localhost/tour-plan/list/all"
        );

        this.plans = response.data;

        // 이메일 가져오기
        const emailResponse = await axios.post(
          "http://localhost/member/getEmailFromToken",
          null,
          {
            headers: {
              "Content-Type": "application/json",
              accessToken: this.accessToken, // 헤더에 accessToken 추가
            },
          }
        );
        this.myEmail = emailResponse.data;

        // 내 ID 가져오기
        const idResponse = await axios.post(
          `http://localhost/member/getMemberIdFromEmail?email=${this.myEmail}`
        );
        this.myId = idResponse.data;

        // ownerId와 myId가 같은 항목 필터링
        this.myPlans = this.plans.filter((plan) => plan.ownerId === this.myId);
      } catch (error) {
        console.error("데이터를 불러오는 데 실패했습니다:", error);
      } finally {
        this.loading = false;
      }
    },
    updateShowAll() {
      const routeName = this.$route.name;
      this.showAll = routeName === "PlanListAll";
    },
  },
  watch: {
    $route(to, from) {
      this.updateShowAll();
    },
  },
  mounted() {
    this.fetchData();
    this.updateShowAll();
  },
};
</script>

<style scoped>
#pink {
  background-color: #f0dbe2;
  display: flex;
  justify-content: center;
  align-items: center;
}

#white {
  background-color: #fff;
  border-radius: 3%;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 960px;
  width: 100%;
}
</style>
