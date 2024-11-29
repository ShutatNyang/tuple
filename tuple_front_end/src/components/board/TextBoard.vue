<template>
  <div class="px-10 flex flex-1 justify-center py-5">
    <div class="layout-content-container flex flex-col max-w-[960px] flex-1">
      <!-- 디버그: 전체 데이터 확인 -->

      <!-- 공지사항 게시물 리스트 -->
      <div v-if="boardType === 'notice'">
        <div v-for="item in items" :key="item.id" class="mb-4">
          <TextBoardItem :item="item" :boardName="boardType" />
        </div>
      </div>

      <!-- 자유게시판 게시물 리스트 -->
      <div v-else-if="boardType === 'free'">
        <div v-for="item in items" :key="item.id" class="mb-4">
          <TextBoardItem :item="item" :boardName="boardType" />
        </div>
      </div>

      <!-- Q&A 게시판 -->
      <div v-else-if="boardType === 'qna'">
        <div v-for="item in items" :key="item.id" class="mb-4">
          <TextBoardItem :item="item" :boardName="boardType" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

import TextBoardItem from "./item/TextBoardItem.vue";

export default {
  name: "TextBoard",
  components: {
    TextBoardItem,
  },
  props: {
    boardType: {
      type: String,
      required: true,
      validator: (value) => ["notice", "free", "qna"].includes(value),
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
      // 공지사항 또는 자유게시판의 아이템 데이터
      items: [],
      size: 10,
    };
  },
  created() {
    this.fetchBoardItems();
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
        this.items = response.data.sort((a, b) => b.id - a.id);
      } catch (error) {
        console.error("게시글 데이터를 가져오는 데 실패했습니다:", error);
      }
    },
  },
};
</script>

<style scoped>
.line-clamp-1 {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.line-clamp-2 {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
