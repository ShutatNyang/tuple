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
          <!-- Hero Section -->
          <HeroSection />

          <!-- Featured Destinations Section -->
          <MainPageMatch :Matchings="tourMatching" />

          <!-- Top Trending Section -->
          <TopTrending :topTrending="topTrending" />

          <!-- Explore By Interest Section -->
          <BestReview :BestReview="bestReviews" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import BestReview from "../main/BestReview.vue";
import HeroSection from "../main/HeroSection.vue";
import MainPageMatch from "../main/MainPageMatch.vue";
import TopTrending from "../main/MainPageTopTrending.vue";

export default {
  data() {
    return {
      tourMatchIds: [],
      tourMatching: [],
      bestReviews: [],
    };
  },
  props: {
    activeSection: {
      type: String,
      required: true,
    },
  },
  components: {
    HeroSection,
    MainPageMatch,
    TopTrending,
    BestReview,
  },
  methods: {
    async fetchMatching() {
      try {
        const response = await axios.post(
          "http://localhost/tour-match/list/await",
          null,
          {
            params: {
              memberId: -1, // 현재 사용자 제외
            },
          }
        );

        this.tourMatchIds = response.data
          .filter((match) => match.status === "pending")
          .map((match) => match.tourPlanId);
      } catch (error) {
        console.error("매칭 데이터를 불러오는 중 오류 발생:", error);
        alert("매칭 데이터를 불러오는 데 실패했습니다.");
      }
    },

    async fetchFilteredTourPlans() {
      try {
        const response = await axios.post(
          "http://localhost/tour-plan/list/all"
        ); // 모든 여행 계획 가져오기

        // tour_plan_id와 매칭된 데이터만 필터링
        this.tourMatching = response.data.filter((plan) =>
          this.tourMatchIds.includes(plan.id)
        );
      } catch (error) {
        console.error("여행 계획 데이터를 불러오는 중 오류 발생:", error);
        alert("여행 계획 데이터를 불러오는 데 실패했습니다.");
      }
    },

    async fetchReviews() {
      try {
        const response = await axios.post(
          "http://localhost/board/list/category",
          null,
          {
            params: {
              boardType: "review",
              page: 1,
              size: 4,
            },
          }
        );
        this.bestReviews = response.data;
      } catch (error) {
        console.error("매칭 데이터를 불러오는 중 오류 발생:", error);
        alert("매칭 데이터를 불러오는 데 실패했습니다.");
      }
    },
  },

  async mounted() {
    await this.fetchMatching();
    await this.fetchFilteredTourPlans();
    await this.fetchReviews();
  },
};
</script>

<style scoped>
#top-trending {
  padding: 1rem;
  background-color: #f9f9f9;
  border-radius: 8px;
}
#pink {
  background-color: #f0dbe2;
}
#white {
  background-color: #fff;
  border-radius: 3%;
}
</style>
