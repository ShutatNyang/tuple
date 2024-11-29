<template>
  <div class="px-10 flex flex-1 justify-center py-5">
    <div class="layout-content-container flex flex-col max-w-[960px] flex-1">
      <!-- 제목 -->
      <h2
        class="text-[#181013] text-[22px] font-bold leading-tight tracking-[-0.015em] px-4 pb-3 pt-5"
      >
        베스트 후기
      </h2>

      <!-- 게시물 리스트 -->
      <div class="p-4" v-for="(review, index) in BestReview" :key="index">
        <ImageBoardItem :item="review" />
      </div>
    </div>
  </div>
</template>

<script>
import ImageBoardItem from "../board/item/ImageBoardItem.vue";

export default {
  props: {
    BestReview: {
      type: Object,
      required: true,
    },
  },
  components: {
    ImageBoardItem,
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
.p-4 {
  padding: 16px; /* 적절한 패딩을 추가 */
}
</style>
