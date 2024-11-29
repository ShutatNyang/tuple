<template>
  <div class="px-10 flex flex-1 justify-center py-5">
    <div class="layout-content-container flex flex-col max-w-[960px] flex-1">
      <!-- 내 매칭 리스트 -->
      <h2 class="text-lg font-semibold mb-4">내 여행 계획</h2>
      <div v-if="myPlans.length === 0">
        <span>내 여행 계획이 없습니다!</span>
      </div>
      <div class="mb-3">
        <ul class="my-plans my-plans-container">
          <li
            v-for="plan in myPlans"
            :key="plan.id"
            class="my-plan-item cursor-pointer"
          >
            <MyPlanItem
              :plan="plan"
              :myId="myId"
              @accepted="fetchData"
              :status="plan.status"
            />

            <div
              v-if="plan.status === 'matched' || plan.status === 'rejected'"
              class="status-icon"
            >
              <!-- 수락 -->
              <svg
                v-if="plan.status === 'matched'"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 60 60"
                class="icon accept"
              >
                <path
                  d="M800,510a30,30,0,1,1,30-30A30,30,0,0,1,800,510Zm-16.986-23.235a3.484,3.484,0,0,1,0-4.9l1.766-1.756a3.185,3.185,0,0,1,4.574.051l3.12,3.237a1.592,1.592,0,0,0,2.311,0l15.9-16.39a3.187,3.187,0,0,1,4.6-.027L817,468.714a3.482,3.482,0,0,1,0,4.846l-21.109,21.451a3.185,3.185,0,0,1-4.552.03Z"
                  transform="translate(-770 -450)"
                />
              </svg>
              <span
                v-if="plan.status === 'matched'"
                class="icon accept text-[#699f4c] font-extrabold"
                >매칭 성공!</span
              >

              <!-- 거절 -->
              <svg
                v-if="plan.status === 'rejected'"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 60 60"
                class="icon reject"
              >
                <path
                  d="M100,390a30,30,0,1,1,30-30A30,30,0,0,1,100,390Zm18-30a4,4,0,0,1-4,4H86a4,4,0,0,1,0-8h28A4,4,0,0,1,118,360Z"
                  transform="translate(-70 -330)"
                />
              </svg>
              <span
                v-if="plan.status === 'rejected'"
                class="icon reject text-[#9f4c4c] font-extrabold"
                >매칭 실패 ㅠ.ㅠ</span
              >
            </div>
          </li>
        </ul>
      </div>

      <!-- 내가 요청한 매칭 리스트 -->
      <h2 class="text-lg font-semibold mb-4">요청한 매칭</h2>
      <div v-if="requestPlans.length === 0">
        <span>요청한 매칭이 없습니다!</span>
      </div>
      <ul>
        <li
          v-for="plan in requestPlans"
          :key="plan.id"
          class="mb-4 cursor-pointer relative"
        >
          <MatchingItem :plan="plan" />

          <!-- 아이콘 -->
          <div
            v-if="plan.status === 'matched' || plan.status === 'rejected'"
            class="status-icon"
          >
            <!-- 수락 -->
            <svg
              v-if="plan.status === 'matched'"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 60 60"
              class="icon accept"
            >
              <path
                d="M800,510a30,30,0,1,1,30-30A30,30,0,0,1,800,510Zm-16.986-23.235a3.484,3.484,0,0,1,0-4.9l1.766-1.756a3.185,3.185,0,0,1,4.574.051l3.12,3.237a1.592,1.592,0,0,0,2.311,0l15.9-16.39a3.187,3.187,0,0,1,4.6-.027L817,468.714a3.482,3.482,0,0,1,0,4.846l-21.109,21.451a3.185,3.185,0,0,1-4.552.03Z"
                transform="translate(-770 -450)"
              />
            </svg>
            <span
              v-if="plan.status === 'matched'"
              class="icon accept text-[#699f4c] font-extrabold"
              >매칭 성공!</span
            >

            <!-- 거절 -->
            <svg
              v-if="plan.status === 'rejected'"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 60 60"
              class="icon reject"
            >
              <path
                d="M100,390a30,30,0,1,1,30-30A30,30,0,0,1,100,390Zm18-30a4,4,0,0,1-4,4H86a4,4,0,0,1,0-8h28A4,4,0,0,1,118,360Z"
                transform="translate(-70 -330)"
              />
            </svg>
            <span
              v-if="plan.status === 'rejected'"
              class="icon reject text-[#9f4c4c] font-extrabold"
              >매칭 실패 ㅠ.ㅠ</span
            >
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import MatchingItem from "@/components/match/item/MatchingItem.vue";
import MyPlanItem from "@/components/match/item/MyPlanItem.vue";
import axios from "axios";

export default {
  name: "MatchingList",
  components: {
    MatchingItem,
    MyPlanItem,
  },
  props: {
    myId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      requestPlans: [],
      requestTourMatchIds: [],
      myPlans: [],
      myTourMatchIds: [],
    };
  },
  mounted() {
    this.waitForMyId().then(() => {
      this.fetchData();
    });
  },
  methods: {
    async waitForMyId() {
      while (!this.myId) {
        await new Promise((resolve) => setTimeout(resolve, 100)); // myId가 준비될 때까지 대기
      }
    },

    async fetchData() {
      try {
        await this.fetchMyPlansIds();
        await this.fetchFilteredMyPlans();
        await this.fetchRequestPlansIds();
        await this.fetchFilteredMyRequestPlans();
      } catch (error) {
        console.error("데이터를 불러오는 중 오류 발생:", error);
      }
    },

    async fetchMyPlansIds() {
      try {
        const response = await axios.post(
          "http://localhost/tour-match/list/my-match",
          null,
          {
            params: {
              memberId: this.myId,
            },
          }
        );

        // 각 tourPlanId와 status를 매핑하여 저장
        this.myTourMatchIds = response.data.map((match) => ({
          tourPlanId: match.tourPlanId,
          status: match.status,
        }));
      } catch (error) {
        console.error("매칭 데이터를 불러오는 중 오류 발생:", error);
        alert("매칭 데이터를 불러오는 데 실패했습니다.");
      }
    },

    async fetchFilteredMyPlans() {
      try {
        const response = await axios.post(
          "http://localhost/tour-plan/list/all"
        ); // 모든 여행 계획 가져오기
        const allPlans = response.data;

        // 매칭된 데이터와 상태를 추가
        this.myPlans = allPlans
          .filter((plan) =>
            this.myTourMatchIds.some((match) => match.tourPlanId === plan.id)
          )
          .map((plan) => {
            const matchedStatus = this.myTourMatchIds.find(
              (match) => match.tourPlanId === plan.id
            )?.status;
            return { ...plan, status: matchedStatus }; // status를 추가
          });
      } catch (error) {
        console.error("여행 계획 데이터를 불러오는 중 오류 발생:", error);
        alert("여행 계획 데이터를 불러오는 데 실패했습니다.");
      }
    },

    async fetchRequestPlansIds() {
      try {
        const response = await axios.post(
          "http://localhost/tour-match/list/request",
          null,
          {
            params: {
              partnerId: this.myId,
            },
          }
        );

        // 각 tourPlanId와 status를 매핑하여 저장
        this.requestTourMatchIds = response.data.map((match) => ({
          tourPlanId: match.tourPlanId,
          status: match.status,
        }));
      } catch (error) {
        console.error("매칭 데이터를 불러오는 중 오류 발생:", error);
        alert("매칭 데이터를 불러오는 데 실패했습니다.");
      }
    },

    async fetchFilteredMyRequestPlans() {
      try {
        const response = await axios.post(
          "http://localhost/tour-plan/list/all"
        ); // 모든 여행 계획 가져오기
        const allPlans = response.data;

        // 매칭된 데이터와 상태를 추가
        this.requestPlans = allPlans
          .filter((plan) =>
            this.requestTourMatchIds.some(
              (match) => match.tourPlanId === plan.id
            )
          )
          .map((plan) => {
            const matchedStatus = this.requestTourMatchIds.find(
              (match) => match.tourPlanId === plan.id
            )?.status;
            return { ...plan, status: matchedStatus }; // status를 추가
          });
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

li {
  position: relative; /* MatchingItem 기준 */
}

.status-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 10;
}

.icon {
  width: 100px;
  height: 100px;
  opacity: 0.8;
}

.accept {
  fill: #699f4c;
  fill-rule: evenodd;
}

.reject {
  fill: #c71d1d;
  fill-rule: evenodd;
}

.my-plans-container {
  display: flex;
  flex-wrap: wrap; /* 다음 줄로 밀어내기 */
  gap: 20px; /* 간격 */
}

.my-plan-item {
  flex: 0 0 calc(33.333% - 20px); /* 한 줄에 3개, 간격 제외 */
  box-sizing: border-box;
}
</style>
