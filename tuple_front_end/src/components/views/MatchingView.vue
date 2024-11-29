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
          <MatchingHeader />

          <RouterView :my-id="myId" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MatchingHeader from "../match/MatchingHeader.vue";
import { RouterView } from "vue-router";
import axios from "axios";

export default {
  components: { MatchingHeader, RouterView },
  name: "MatchingView",
  components: {
    MatchingHeader,
    RouterView,
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
  methods: {
    async fetchData() {
      this.error = null;

      try {
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
      } catch (error) {
        console.error("데이터를 불러오는 데 실패했습니다:", error);
        this.error = "데이터를 불러오는 데 실패했습니다.";
      }
    },
  },
  mounted() {
    this.fetchData();
  },
};
</script>

<style scoped>
#pink {
  background-color: #f0dbe2; /* 핑크색 배경 */
  display: flex;
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
}

#white {
  background-color: #fff; /* 하얀색 박스 */
  border-radius: 3%; /* 둥근 모서리 */
  padding: 20px; /* 내부 여백 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 박스 그림자 */
  max-width: 960px; /* 최대 너비 */
  width: 100%; /* 반응형 너비 */
}
</style>
