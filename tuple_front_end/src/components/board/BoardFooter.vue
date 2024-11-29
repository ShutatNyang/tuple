<template>
  <div>
    <!-- Pagination Centered -->
    <div class="flex items-center justify-center px-4">
      <div class="flex gap-2">
        <a
          href="#"
          class="flex size-10 items-center justify-center"
          @click.prevent="prevPage"
        >
          <div
            class="text-[#181112]"
            data-icon="CaretLeft"
            data-size="18px"
            data-weight="regular"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="18px"
              height="18px"
              fill="currentColor"
              viewBox="0 0 256 256"
            >
              <path
                d="M165.66,202.34a8,8,0,0,1-11.32,11.32l-80-80a8,8,0,0,1,0-11.32l80-80a8,8,0,0,1,11.32,11.32L91.31,128Z"
              ></path>
            </svg>
          </div>
        </a>

        <a
          v-for="page in totalPages"
          :key="page"
          href="#"
          class="text-sm flex size-10 items-center justify-center rounded-full"
          :class="{
            'font-bold bg-[#f5f0f1]': page === currentPage,
            'font-normal': page !== currentPage,
          }"
          @click.prevent="goToPage(page)"
        >
          {{ page }}
        </a>

        <a
          href="#"
          class="flex size-10 items-center justify-center"
          @click.prevent="nextPage"
        >
          <div
            class="text-[#181112]"
            data-icon="CaretRight"
            data-size="18px"
            data-weight="regular"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="18px"
              height="18px"
              fill="currentColor"
              viewBox="0 0 256 256"
            >
              <path
                d="M181.66,133.66l-80,80a8,8,0,0,1-11.32-11.32L164.69,128,90.34,53.66a8,8,0,0,1,11.32-11.32l80,80A8,8,0,0,1,181.66,133.66Z"
              ></path>
            </svg>
          </div>
        </a>
      </div>
    </div>

    <!-- Write Button Aligned Right -->
    <div class="flex justify-end px-4 py-2">
      <button
        class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#fc6986] text-white text-sm font-bold leading-normal tracking-[0.015em]"
        @click="goToWritePage"
      >
        <span class="truncate">Write</span>
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { ref, watch } from "vue";
import { useRoute } from "vue-router";

export default {
  setup() {
    const route = useRoute(); // 현재 라우트 정보 가져오기
    const boardType = ref(route.path.split("/")[2]); // props에서 boardType 가져오기

    watch(
      () => route.path, // path가 변경될 때 감지
      (newPath) => {
        boardType.value = newPath.split("/")[2];
      }
    );

    return {
      boardType,
    };
  },
  data() {
    return {
      currentPage: 1,
      totalPages: 5, // 총 페이지 수를 필요에 따라 조정하세요
    };
  },
  mounted() {
    // 초기 마운트 시 현재 boardType으로 API 호출
    this.fetchTotalPages(this.boardType);
  },
  watch: {
    boardType(newType) {
      this.fetchTotalPages(newType);
    },
  },
  methods: {
    async fetchTotalPages(type) {
      try {
        const response = await axios.post(
          "http://localhost/board/list/all",
          null,
          {
            params: {
              boardType: type,
            },
          }
        );

        const totalItems = response.data;

        if (["event", "review"].includes(type)) {
          this.totalPages = Math.ceil(totalItems / 6);
        } else if (["notice", "free", "qna"].includes(type)) {
          this.totalPages = Math.ceil(totalItems / 10);
        } else {
          this.totalPages = 0;
        }
      } catch (error) {
        console.error("API 호출 중 오류 발생:", error);
        this.totalPages = 0;
      }
    },
    goToWritePage() {
      this.$router.push(`/board/${this.boardType}/write`);
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.emitPageUpdate();
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.emitPageUpdate();
      }
    },
    goToPage(page) {
      this.currentPage = page;
      this.emitPageUpdate();
    },
    emitPageUpdate() {
      this.$emit("update-page", this.currentPage);
    },
  },
};
</script>

<style scoped>
/* Tailwind 기본 스타일을 사용 중이므로 추가 스타일 필요 시 작성 */
</style>
