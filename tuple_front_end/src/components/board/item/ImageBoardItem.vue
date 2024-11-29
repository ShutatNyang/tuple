<!-- src/components/board/EventBoardItem.vue -->
<template>
  <div
    class="flex items-stretch justify-between gap-4 rounded-xl border border-gray-400 my-2 p-2 cursor-pointer"
    @click="goToDetail"
  >
    <!-- 텍스트 섹션 -->
    <div class="flex flex-[2_2_0px] flex-col gap-4">
      <div class="flex flex-col gap-1">
        <p class="text-[#181013] text-base font-bold leading-tight">
          {{ item.title }}
        </p>
        <p class="text-[#8d5e6d] text-sm font-normal leading-normal">
          {{ item.email }} / {{ formatDate(item.registTime) }}
        </p>
        <p class="text-[#8d5e6d] text-sm font-normal leading-normal">
          추천 : {{ item.likeCount }} 비추천
          {{ item.dislikeCount }}
        </p>
      </div>
    </div>
    <!-- 이미지 섹션 -->
    <div
      class="flex-1 bg-center bg-no-repeat aspect-video bg-cover rounded-xl"
      :style="{ backgroundImage: `url(${item.thumbnailUrl})` }"
    ></div>
  </div>
</template>

<script>
import { useRouter } from "vue-router";

export default {
  name: "ImageBoardItem",

  data() {
    return {
      markdownContent: "",
    };
  },

  props: {
    item: {
      type: Object,
      required: true,
    },
    boardName: {
      type: String,
      required: true, // 부모에서 전달받은 boardName
    },
    review: {
      type: Object,
    },
  },
  setup(props) {
    const router = useRouter();
    const goToDetail = () => {
      router.push(`/board/${props.boardName}/detail/${props.item.id}`);
    };

    return {
      goToDetail,
    };
  },
  methods: {
    /**
     * 날짜 형식을 보기 좋게 변환하는 메서드
     * @param {string} dateStr - ISO 형식의 날짜 문자열
     * @returns {string} - 변환된 날짜 문자열
     */
    formatDate(dateStr) {
      const date = new Date(dateStr);
      return date.toLocaleDateString("ko-KR", {
        year: "numeric",
        month: "long",
        day: "numeric",
      });
    },
  },
  mounted() {
    // 이미 thumbnailUrl이 있다면 그대로 사용
    if (this.item.thumbnailUrl) {
      return;
    }

    // 마크다운(content)을 파싱하여 이미지 URL 추출
    if (this.item.content) {
      this.markdownContent = this.item.content;

      // DOMParser를 사용하여 HTML로 변환
      const parser = new DOMParser();
      const doc = parser.parseFromString(this.markdownContent, "text/html");

      // 모든 <img> 태그를 검색
      const imgTags = doc.querySelectorAll("img");

      // <img> 태그에서 src 속성을 추출
      const imageLinks = Array.from(imgTags).map((img) => img.src);

      // 첫 번째 이미지 URL을 thumbnailUrl로 설정
      if (imageLinks.length > 0) {
        this.item.thumbnailUrl = imageLinks[0];
      }
    }
  },
};
</script>

<style scoped>
.cursor-pointer {
  cursor: pointer; /* 클릭 가능 표시 */
}
</style>
