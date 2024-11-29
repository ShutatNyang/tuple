<template>
  <div class="write-plan-container">
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
          여행계획작성
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

    <main class="write-plan-main">
      <div class="flex-container">
        <!-- 왼쪽: 계획 작성 폼 -->
        <div class="left-pane">
          <div class="layout-content-container flex flex-col w-full py-5">
            <!-- 계획명 입력 -->
            <div class="flex max-w-full flex-wrap items-end gap-4 px-4 py-3">
              <label class="flex flex-col min-w-40 flex-1">
                <p
                  class="text-[#181112] text-base font-medium leading-normal pb-2"
                >
                  계획명
                </p>
                <input
                  v-model="plan.title"
                  class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white focus:border-[#e6dbdd] h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal leading-normal"
                  placeholder="Enter the title of your plan"
                />
              </label>
            </div>

            <!-- 여행 일정 선택 -->
            <div class="flex max-w-full flex-wrap items-end gap-4 px-4 py-3">
              <label class="flex flex-col w-full">
                <p
                  class="text-[#181112] text-base font-medium leading-normal pb-2"
                >
                  여행 일정
                </p>
                <Datepicker
                  v-model="dateRange"
                  :range="true"
                  :multiCalendars="true"
                  :enableTime="false"
                  format="yyyy/MM/dd"
                  placeholder="시작일과 종료일을 선택해주세요"
                  class="datepicker"
                />
              </label>
            </div>

            <!-- 여행지 입력 -->
            <div class="flex max-w-full flex-wrap items-end gap-4 px-4 py-3">
              <label class="flex flex-col min-w-40 flex-1">
                <p
                  class="text-[#181112] text-base font-medium leading-normal pb-2"
                >
                  여행지
                </p>
                <input
                  v-model="plan.start_location"
                  class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white focus:border-[#e6dbdd] h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal leading-normal"
                  placeholder="Enter the title of your destination"
                />
              </label>
            </div>

            <!-- 교통 수단 선택 -->
            <div class="flex max-w-full flex-wrap items-end gap-4 px-4 py-3">
              <label class="flex flex-col">
                <p
                  class="text-[#181112] text-base font-medium leading-normal pb-2"
                >
                  교통 수단
                </p>
                <select
                  v-model="plan.vehicle"
                  class="form-select flex w-full min-w-0 flex-1 rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white focus:border-[#e6dbdd] h-12 text-base font-normal leading-normal"
                >
                  <option value="" disabled>교통 수단을 선택하세요</option>
                  <option value="own_car">자차</option>
                  <option value="public_transit">대중교통</option>
                  <option value="rental_car">렌터카</option>
                  <option value="other">기타</option>
                </select>
              </label>
            </div>

            <div class="search-container">
              <!-- 렌터카 검색 -->
              <div
                v-if="plan.vehicle === 'rental_car'"
                class="search-container"
              >
                <label class="select-label">
                  <select
                    v-model="selectedSidoName"
                    class="select-input"
                    @change="fetchGuguns()"
                  >
                    <option value="" :selected="!selectedSido" disabled>
                      시도
                    </option>
                    <option
                      v-for="sido in sidos"
                      :key="sido.sidoCode"
                      :value="sido.sidoName"
                    >
                      {{ sido.sidoName }}
                    </option>
                  </select>
                </label>

                <!-- 두 번째 드롭다운: 구군 -->
                <label class="select-label">
                  <select
                    v-model="selectedGugunName"
                    :disabled="isGugunDisabled"
                    class="select-input"
                  >
                    <option value="" disabled>구군</option>
                    <option
                      v-for="gugun in guguns"
                      :key="gugun.gugunCode"
                      :value="gugun.gugunName"
                    >
                      {{ gugun.gugunName }}
                    </option>
                  </select>
                </label>
                <button @click="fetchRentalCar">렌터카 확인</button>
              </div>
            </div>

            <!-- 여행 설명 입력 -->
            <div class="flex max-w-full flex-wrap items-end gap-4 px-4 py-3">
              <label class="flex flex-col min-w-40 flex-1">
                <p
                  class="text-[#181112] text-base font-medium leading-normal pb-2"
                >
                  여행 설명
                </p>
                <textarea
                  v-model="plan.description"
                  class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white focus:border-[#e6dbdd] min-h-36 placeholder:text-[#8c5f68] p-[15px] text-base font-normal leading-normal"
                  placeholder="Describe your plan"
                ></textarea>
              </label>
            </div>

            <!-- 날짜 별 일정 -->
            <h3
              v-if="planItems.length > 0"
              class="text-[#181112] text-lg font-bold leading-tight tracking-[-0.015em] px-4 pb-2 pt-4"
            >
              날짜 별 일정
            </h3>

            <!-- Plan Items -->
            <div v-if="planItems.length > 0" class="flex flex-col px-4">
              <div
                v-for="(item, index) in planItems"
                :key="index"
                class="plan-item-container"
              >
                <PlanItem
                  ref="planItemsRefs"
                  :item="item"
                  :day="index + 1"
                  @update="updatePlanItem(index, $event)"
                  @remove="removePlanItem(index)"
                  @show-results="handleCardClick"
                />
              </div>
            </div>

            <!-- 계획 저장 버튼 -->
            <div class="flex px-4 py-3 justify-end">
              <button
                class="flex min-w-[84px] max-w-full cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#fc6986] text-white text-sm font-bold leading-normal tracking-[0.015em]"
                @click="savePlan"
              >
                Save Plan
              </button>
            </div>
          </div>
        </div>

        <!-- 오른쪽: TourPlanSidebar -->
        <div class="right-pane">
          <TourPlanSidebar
            :selectedResult="selectedResult"
            @update:selectedResult="updateSelectedResult"
            :rentalCarResults="rentalCarResults"
          />
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";
import axios from "axios";
import PlanItem from "./item/PlanItem.vue";
import TourPlanSidebar from "./item/TourPlanSidebar.vue";

export default {
  components: {
    Datepicker,
    PlanItem,
    TourPlanSidebar,
  },
  props: {
    id: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      sidos: [],
      guguns: [],
      selectedSido: "",
      selectedSidoName: "",
      selectedGugun: "",
      selectedGugunName: "",
      rentalCarResults: [],
      plan: {
        title: "",
        description: "",
        start_location: "",
        startDate: null,
        endDate: null,
      },
      dateRange: [],
      planItems: [],
      selectedResult: null,
      ownerId: null, // 추가된 사용자 ID 필드
    };
  },

  watch: {
    dateRange(newRange) {
      if (Array.isArray(newRange) && newRange.length === 2) {
        const [start, end] = newRange;
        if (new Date(start) > new Date(end)) {
          alert("시작일이 종료일보다 이후일 수 없습니다.");
          this.dateRange = [end, start];
        } else {
          this.plan.startDate = start;
          this.plan.endDate = end;
          this.generatePlanItems();
        }
      } else {
        this.plan.startDate = null;
        this.plan.endDate = null;
        this.planItems = [];
      }
    },
  },
  mounted() {
    const token = sessionStorage.getItem("accessToken");
    if (token) {
      this.getMemberInfo(token);
    } else {
      alert("로그인이 필요합니다.");
      this.$router.push("/member/login");
    }

    if (this.id) {
      this.fetchPlanData();
    }

    this.fetchSidos();
  },
  methods: {
    async fetchSidos() {
      try {
        const response = await axios.get("http://localhost/trip/getSidos");
        if (response.status === 200) {
          this.sidos = response.data;
        }
      } catch (error) {
        console.error("시도 데이터를 가져오지 못했습니다:", error);
      }
    },
    async fetchGuguns() {
      if (!this.selectedSidoName) return;

      const sido = this.sidos.find(
        (sido) => sido.sidoName === this.selectedSidoName
      );
      try {
        const response = await axios.get("http://localhost/trip/getGuguns", {
          params: { sidoCode: sido.sidoCode },
        });
        if (response.status === 200) {
          this.guguns = response.data;
        }
      } catch (error) {
        console.error("구군 데이터를 가져오지 못했습니다:", error);
      }
    },
    async getMemberInfo(token) {
      const url = "http://localhost/member/getMemberInfo"; // API 경로 설정
      try {
        const response = await axios.post(
          url,
          {},
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );
        const data = response.data;
        this.ownerId = data.id; // 사용자 ID 설정
      } catch (error) {
        console.error("회원 정보 가져오기 실패:", error);
        if (error.response?.status === 401) {
          alert("세션이 만료되었습니다. 다시 로그인해주세요.");
          this.$router.push("/member/login");
        } else {
          alert("회원 정보를 가져오는 중 오류가 발생했습니다.");
        }
      }
    },
    generatePlanItems() {
      if (this.plan.startDate && this.plan.endDate) {
        const start = new Date(this.plan.startDate);
        const end = new Date(this.plan.endDate);
        const timeDiff = end.getTime() - start.getTime();
        const dayCount = Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1;

        if (this.planItems.length !== dayCount) {
          this.planItems = [];
          for (let i = 0; i < dayCount; i++) {
            this.planItems.push({
              day: i + 1,
              details: "",
              results: [],
              shortDescription: "",
            });
          }
        }
      }
    },
    updatePlanItem(index, updatedItem) {
      this.planItems[index] = updatedItem;
    },
    removePlanItem(index) {
      this.planItems.splice(index, 1);
      this.planItems.forEach((item, idx) => {
        item.day = idx + 1;
      });
    },
    handleCardClick(result) {
      if (result) {
        this.selectedResult = result;
      }
    },
    updateSelectedResult(result) {
      this.selectedResult = result;
    },

    async fetchPlanData() {
      try {
        const response = await axios.post(
          `http://localhost/tour-plan/detail`,
          null,
          {
            params: { id: this.id },
          }
        );

        const planData = response.data;

        // 폼에 기존 데이터 채우기
        this.plan.title = planData.planTitle;
        this.plan.description = planData.planDescription;
        this.plan.start_location = planData.startLocation;
        this.plan.vehicle = planData.vehicle;
        this.plan.startDate = new Date(planData.startDate);
        this.plan.endDate = new Date(planData.endDate);
        this.dateRange = [this.plan.startDate, this.plan.endDate];

        // planItems 설정
        if (typeof planData.plan === "string") {
          planData.plan = JSON.parse(planData.plan);
        }

        if (planData.plan && Array.isArray(planData.plan.days)) {
          this.planItems = planData.plan.days.map((dayPlan) => {
            return {
              day: dayPlan.day,
              details: dayPlan.details || "",
              results: dayPlan.locations || [],
              shortDescription: dayPlan.shortDescription || "",
            };
          });
        }
      } catch (error) {
        console.error("계획 데이터를 불러오는 데 실패했습니다:", error);
      }
    },

    savePlan() {
      if (!this.plan.title) {
        alert("계획명을 입력해주세요.");
        return;
      }
      if (!this.plan.description) {
        alert("여행 설명을 입력해주세요.");
        return;
      }
      if (!this.plan.startDate || !this.plan.endDate) {
        alert("시작일과 종료일을 선택해주세요.");
        return;
      }
      if (!this.ownerId) {
        alert("회원 정보가 올바르지 않습니다. 다시 로그인해주세요.");
        return;
      }

      // 자식 컴포넌트 데이터를 가져오기
      const collectedPlanItems = this.$refs.planItemsRefs.map((ref) =>
        ref.getItemData()
      );

      // 데이터 포맷
      const formattedPlanItems = collectedPlanItems.map((item, index) => ({
        date: new Date(this.plan.startDate.getTime() + index * 86400000)
          .toISOString()
          .split("T")[0],
        locations: item.results.map((result) => ({
          memo: result.memo || "",
          name: result.title,
          image: result.firstImage1 || "https://via.placeholder.com/150", // 기본 이미지 추가
          latitude: result.latitude || null, // 위도 추가
          longitude: result.longitude || null, // 경도 추가
        })),
        shortDescription: item.shortDescription || "", // shortDescription 추가
        accommodation: item.accommodation
          ? {
              memo: item.accommodation.memo || "",
              name: item.accommodation.name,
            }
          : null,
      }));

      const requestBody = {
        id: this.id ? this.id : 0,
        ownerId: this.ownerId, // 가져온 사용자 ID 사용
        planTitle: this.plan.title,
        startDate: this.plan.startDate.toISOString().split("T")[0],
        endDate: this.plan.endDate.toISOString().split("T")[0],
        startLocation: this.plan.start_location,
        vehicle: this.plan.vehicle || "other",
        toTourVehicle: this.plan.toTourVehicle || "other",
        plan: JSON.stringify({ days: formattedPlanItems }), // 포맷팅된 JSON 데이터
        planDescription: this.plan.description,
      };

      const apiEndpoint = this.id
        ? "http://localhost/tour-plan/update"
        : "http://localhost/tour-plan/insert";

      axios
        .post(apiEndpoint, requestBody, {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
          },
        })
        .then(() => {
          alert("계획이 성공적으로 저장되었습니다!");
          this.$router.push("/tour-plan/my-plan");
        })
        .catch((error) => {
          alert("저장 실패");
          console.error(error);
        });
    },
    async fetchRentalCar() {
      if (!this.selectedSidoName) {
        alert("시도를 선택해주세요.");
        return;
      }

      const requestBody = {
        sidoName: this.selectedSidoName,
        gugunName: this.selectedGugunName,
      };

      try {
        const response = await axios.post(
          "http://localhost/car-rental/list/getByCodes",
          requestBody
        );

        if (response.data) {
          this.rentalCarResults = response.data;
        } else {
          alert("검색 결과가 없습니다.");
        }
      } catch (error) {
        console.error("렌터카 검색 중 에러 발생:", error);
        alert("렌터카 검색 중 문제가 발생했습니다.");
      }
    },
    goToPlan() {
      this.$router.push("/tour-plan");
    },
  },
};
</script>

<style scoped>
/* 전체 컨테이너 */
.write-plan-container {
  display: flex;
  flex-direction: column;
  height: 95vh;
  overflow: hidden;
  margin-left: 5%;
  margin-right: 5%;
}

/* 헤더 */
.write-plan-header {
  text-align: center;
  margin-bottom: 20px;
}

.write-plan-header h1 {
  font-size: 2.5rem;
  font-weight: bold;
  color: #181112;
}

/* 메인 레이아웃 */
.write-plan-main {
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: flex-start;
  overflow: hidden;
}

/* 레이아웃 컨테이너 */
.flex-container {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 100%;
  gap: 20px;
}

/* 왼쪽 컴포넌트 */
.left-pane {
  flex: 1;
  height: 100%;
  overflow-y: auto;
  background-color: #ffffff;
  transition: flex 0.3s ease;
}

/* 오른쪽 컴포넌트 */
.right-pane {
  flex: 2;
  height: 100%;
  overflow-y: auto;
  background-color: #ffffff;
  border-left: 1px solid #e6dbdd;
}

/* 폼 레이아웃 */
.layout-content-container {
  width: 100%;
  max-width: 720px;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

/* Datepicker 스타일 */
.datepicker {
  width: 100%;
  min-height: 56px;
  padding: 12px 16px;
  border: 1px solid #e6dbdd;
  border-radius: 12px;
  font-size: 16px;
  color: #181112;
}

/* 검색 영역 스타일 */
.search-container {
  padding: 3%;
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
  align-items: flex-end;
}
.select-input,
.text-input {
  padding: 8px 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 120px;
}
.select-label,
.text-input-label {
  display: flex;
  flex-direction: column;
}
</style>
