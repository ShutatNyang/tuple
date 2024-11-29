<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden"
    style="font-family: 'Plus Jakarta Sans', 'Noto Sans', sans-serif"
  >
    <div class="px-20 flex flex-1 justify-center py-5">
      <div class="layout-content-container flex flex-col max-w-[960px] flex-1">
        <h1
          class="text-[#181112] text-[22px] font-bold leading-tight tracking-[-0.015em] px-3 text-left pb-3 pt-3"
        >
          {{ plan.planTitle }}
        </h1>
        <p
          class="text-[#8c5f68] text-sm font-normal leading-normal pb-3 pt-1 px-4"
        >
          작성자: {{ writer }}
        </p>
        <p
          class="text-[#8c5f68] text-sm font-normal leading-normal pb-3 pt-1 px-4"
        >
          기간 : {{ plan.startDate }} ~ {{ plan.endDate }}
        </p>
        <p
          class="text-[#8c5f68] text-sm font-normal leading-normal pb-3 pt-1 px-4"
        >
          여행지 : {{ plan.startLocation }}
        </p>
        <p
          class="text-[#8c5f68] text-sm font-normal leading-normal pb-3 pt-1 px-4"
        >
          교통수단 : {{ plan.vehicle }}
        </p>
        <p
          class="text-[#8c5f68] text-sm font-normal leading-normal pb-3 pt-1 px-4"
        >
          {{ plan.planDescription }}
        </p>

        <!-- 여행지 카드 렌더링 -->
        <div
          v-for="(dailyPlan, index) in dailyPlans"
          :key="index"
          class="px-4 py-2"
        >
          <h3
            class="text-[#181112] text-lg font-bold leading-tight tracking-[-0.015em]"
          >
            Day {{ index + 1 }} - {{ dailyPlan.date }}
          </h3>
          <p class="short-description">
            {{ dailyPlan.shortDescription || "" }}
          </p>
          <br />
          <div class="result-cards">
            <ResultCard
              v-for="(location, idx) in dailyPlan.locations"
              :key="idx"
              :result="{
                title: location.name,
                firstImage1: location.image || 'default-image.png',
                memo: location.memo || '',
              }"
              :is-first="idx === 0"
            />
          </div>
        </div>

        <div class="flex px-4 py-3 justify-end gap-4">
          <button
            class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-5 bg-[#fc6986] text-white text-base font-bold leading-normal tracking-[0.015em]"
            @click="handleRequestMatch"
          >
            같이 여행가요!
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ResultCard from "@/components/tour-plan/item/ResultCard.vue";
import axios from "axios";

export default {
  components: {
    ResultCard,
  },
  props: {
    id: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      plan: {},
      writer: "",
      dailyPlans: [],
      memberId: null, // 현재 로그인된 사용자 ID
    };
  },
  async mounted() {
    await this.initialize();
  },
  methods: {
    /**
     * 초기화: 계획 정보와 회원 정보 가져오기
     */
    async initialize() {
      try {
        const token = sessionStorage.getItem("accessToken");

        if (!token) {
          alert("로그인이 필요합니다.");
          this.$router.push("/member/login");
          return;
        }

        await this.fetchPlanDetail();
        await this.getMemberInfo(token);
        await this.getEmailById();
      } catch (error) {
        console.error("초기화 실패:", error);
      }
    },

    /**
     * 로그인한 사용자 정보 가져오기
     */
    async getMemberInfo(token) {
      try {
        const url = "http://localhost/member/getMemberInfo";
        const response = await axios.post(
          url,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );

        this.memberId = response.data.id;
      } catch (error) {
        console.error("회원 정보 요청 실패:", error);
      }
    },

    /**
     * 여행 계획 상세 정보 가져오기
     */
    async fetchPlanDetail() {
      try {
        const response = await axios.post(
          `http://localhost/tour-plan/detail`,
          null,
          {
            params: { id: this.id },
          }
        );

        this.plan = response.data;

        if (typeof this.plan.plan === "string") {
          this.plan.plan = JSON.parse(this.plan.plan);
        }

        if (this.plan.plan && Array.isArray(this.plan.plan.days)) {
          this.dailyPlans = this.plan.plan.days;
        }
      } catch (error) {
        console.error("여행 계획 정보를 가져오는 데 실패했습니다:", error);
      }
    },

    /**
     * 작성자 이메일 가져오기
     */
    async getEmailById() {
      try {
        const response = await axios.get(
          "http://localhost/member/getEmailByMemberId",
          {
            params: { ownerId: this.plan.ownerId },
          }
        );

        this.writer = response.data;
      } catch (error) {
        console.error("작성자 이메일 가져오기 실패:", error);
      }
    },

    async handleRequestMatch() {
      if (this.memberId === this.plan.ownerId) {
        alert("비정상적인 접근입니다.");
        return;
      }

      const token = sessionStorage.getItem("accessToken");

      const requestBody = {
        memberId: this.plan.ownerId,
        partnerId: this.memberId,
        tourPlanId: this.plan.id,
      };

      await axios.post("http://localhost/tour-match/request", requestBody, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      alert(
        "매칭 요청이 성공적으로 전송되었습니다! \n상대방의 답장을 기다리세요!"
      );
    },
  },
};
</script>

<style scoped>
.result-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 16px;
}

.short-description {
  margin-top: 8px;
  font-style: italic;
}
</style>
