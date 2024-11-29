<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden"
    style="font-family: 'Plus Jakarta Sans', 'Noto Sans', sans-serif"
  >
    <header
      class="flex items-center justify-between whitespace-nowrap border-b border-solid border-b-[#f5f0f1] px-10 py-1"
    >
      <div class="flex items-center gap-4 text-[#181013]">
        <div class="size-4">
          <!-- SVG 아이콘 -->
          <svg
            viewBox="0 0 48 48"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <!-- SVG Path 생략 -->
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M39.475 21.6262C40.358 21.4363 40.6863 21.5589 40.7581 21.5934C40.7876 21.655 40.8547 21.857 40.8082 22.3336C40.7408 23.0255 40.4502 24.0046 39.8572 25.2301C38.6799 27.6631 36.5085 30.6631 33.5858 33.5858C30.6631 36.5085 27.6632 38.6799 25.2301 39.8572C24.0046 40.4502 23.0255 40.7407 22.3336 40.8082C21.8571 40.8547 21.6551 40.7875 21.5934 40.7581C21.5589 40.6863 21.4363 40.358 21.6262 39.475C21.8562 38.4054 22.4689 36.9657 23.5038 35.2817C24.7575 33.2417 26.5497 30.9744 28.7621 28.762C30.9744 26.5497 33.2417 24.7574 35.2817 23.5037C36.9657 22.4689 38.4054 21.8562 39.475 21.6262ZM4.41189 29.2403L18.7597 43.5881C19.8813 44.7097 21.4027 44.9179 22.7217 44.7893C24.0585 44.659 25.5148 44.1631 26.9723 43.4579C29.9052 42.0387 33.2618 39.5667 36.4142 36.4142C39.5667 33.2618 42.0387 29.9052 43.4579 26.9723C44.1631 25.5148 44.659 24.0585 44.7893 22.7217C44.9179 21.4027 44.7097 19.8813 43.5881 18.7597L29.2403 4.41187C27.8527 3.02428 25.8765 3.02573 24.2861 3.36776C22.6081 3.72863 20.7334 4.58419 18.8396 5.74801C16.4978 7.18716 13.9881 9.18353 11.5858 11.5858C9.18354 13.988 7.18717 16.4978 5.74802 18.8396C4.58421 20.7334 3.72865 22.6081 3.36778 24.2861C3.02574 25.8765 3.02429 27.8527 4.41189 29.2403Z"
              fill="currentColor"
            ></path>
          </svg>
        </div>
        <h2
          class="text-[#181013] text-lg font-bold leading-tight tracking-[-0.015em]"
        >
          여행계획
        </h2>
      </div>
      <div class="flex flex-wrap gap-2 p-4">
        <a
          class="text-[#8c5f68] text-base font-medium leading-normal"
          href="#"
          @click.prevent="goToPlan"
          >여행계획</a
        >
      </div>
    </header>
    <div class="px-20 flex flex-1 justify-center py-5">
      <div class="layout-content-container flex flex-col max-w-[960px] flex-1">
        <h1
          class="text-[#181112] text-[22px] font-bold leading-tight tracking-[-0.015em] px-4 text-left pb-3 pt-5"
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
              @select="handleLocationSelect"
            />
          </div>
        </div>

        <!-- Buttons -->
        <div
          v-if="memberId === plan.ownerId"
          class="flex px-4 py-3 justify-end gap-4"
        >
          <button
            class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-5 bg-[#b3e6b3] text-[#181112] text-base font-bold leading-normal tracking-[0.015em]"
            @click="handleMatch"
          >
            매칭시작
          </button>
          <button
            class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-5 bg-[#fc6986] text-white text-base font-bold leading-normal tracking-[0.015em]"
            @click="handleModify"
          >
            수정
          </button>
          <button
            class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-5 bg-[#f5f0f1] text-[#181112] text-base font-bold leading-normal tracking-[0.015em]"
            @click="handleDelete"
          >
            삭제
          </button>
        </div>

        <div v-else class="flex px-4 py-3 justify-end gap-4">
          <button
            class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-5 bg-[#87CEEB] text-white text-base font-bold leading-normal tracking-[0.015em]"
            @click="handleRequestMatch"
          >
            매칭신청
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import ResultCard from "./item/ResultCard.vue";

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

    /**
     * 매칭 시작 처리
     */
    async handleMatch() {
      try {
        const token = sessionStorage.getItem("accessToken");

        const requestBody = {
          memberId: this.plan.ownerId,
          tourPlanId: this.plan.id,
        };

        await axios.post("http://localhost/tour-match/insert", requestBody, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        alert("매칭이 성공적으로 시작되었습니다!");
      } catch (error) {
        if (error.response?.status === 409) {
          alert("이미 등록된 매칭입니다.");
        } else {
          console.error("매칭 시작 실패:", error);
          alert("매칭 시작에 실패했습니다. 다시 시도해주세요.");
        }
      }
    },

    /**
     * 매칭 요청 처리
     */
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

      await axios.post("http://localhost/tour-match/insert", requestBody, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      alert("매칭 요청이 성공적으로 전송되었습니다!");
    },

    /**
     * 수정 요청 처리
     */
    handleModify() {
      if (this.memberId !== this.plan.ownerId) {
        alert("자신의 여행 계획만 수정할 수 있습니다.");
        return;
      }
      this.$router.push(`/tour-plan/modify/${this.plan.id}`);
    },

    /**
     * 삭제 요청 처리
     */
    async handleDelete() {
      try {
        if (this.memberId !== this.plan.ownerId) {
          alert("자신의 여행 계획만 삭제할 수 있습니다.");
          return;
        }

        const token = sessionStorage.getItem("accessToken");

        await axios.post(`http://localhost/tour-plan/delete`, null, {
          params: { id: this.plan.id },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        alert("여행 계획이 삭제되었습니다.");
        this.$router.push("/tour-plan/my-plan");
      } catch (error) {
        console.error("여행 계획 삭제 중 오류 발생:", error);
        alert("삭제 요청에 실패했습니다. 다시 시도해주세요.");
      }
    },
    goToPlan() {
      this.$router.push("/tour-plan/");
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
