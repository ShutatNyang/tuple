<template>
  <div class="px-10 flex flex-1 justify-center py-5">
    <div class="layout-content-container flex flex-col max-w-[960px] flex-1">
      <!-- 이벤트 게시판 리스트 -->
      <div v-if="boardType === 'event'">
        <div class="p-4" v-for="item in items" :key="item.id">
          <ImageBoardItem :item="item" :boardName="boardType" />
        </div>
      </div>

      <!-- 리뷰 게시판 리스트 -->
      <div v-else-if="boardType === 'review'">
        <div class="p-4" v-for="item in items" :key="item.id">
          <ImageBoardItem :item="item" :boardName="boardType" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

import ImageBoardItem from "./item/ImageBoardItem.vue";

export default {
  name: "ImageBoard",
  components: {
    ImageBoardItem,
  },
  props: {
    boardType: {
      type: String,
      required: true,
      validator: (value) => ["event", "review"].includes(value),
    },
    updatePage: {
      type: Number,
      required: true,
    },
  },
  watch: {
    boardType(newType, oldType) {
      if (newType !== oldType) {
        this.fetchBoardItems();
      }
    },
    updatePage(newPage, oldPage) {
      if (newPage !== oldPage) {
        this.fetchBoardItems();
      }
    },
  },
  data() {
    return {
      items: [], // 모든 게시물 데이터
      size: 6,
    };
  },
  created() {
    this.fetchBoardItems();
  },
  methods: {
    async fetchBoardItems() {
      try {
        const response = await axios.post(
          "http://localhost/board/list/category",
          null,
          {
            params: {
              boardType: this.boardType,
              page: this.updatePage,
              size: this.size,
            },
          }
        );

        // 각 아이템에 thumbnailUrl을 추가
        this.items = response.data.map((item) => {
          let thumbnailUrl = "";
          if (item.content) {
            const tempDiv = document.createElement("div");
            tempDiv.innerHTML = item.content;
            const img = tempDiv.querySelector("img");
            if (img && img.src) {
              thumbnailUrl = this.normalizeUrl(img.src);
            }
          }
          return {
            ...item,
            thumbnailUrl,
          };
        });

        this.items.sort((a, b) => b.id - a.id);
      } catch (error) {
        console.error("게시글 데이터를 가져오는 데 실패했습니다:", error);
      }
    },
    normalizeUrl(url) {
      return url.endsWith("/") ? url.slice(0, -1) : url;
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
</style>
