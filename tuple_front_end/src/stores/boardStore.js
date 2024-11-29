// src/stores/boardStore.js
import axios from "axios";
import { defineStore } from "pinia";

export const useBoardStore = defineStore("board", {
  state: () => ({
    items: [],
    currentPage: 1,
    itemsPerPage: 10,
    totalPages: 1,
    boardType: "", // Pinia의 state에 boardType 저장
    error: null,
  }),
  actions: {
    setBoardType(type) {
      if (this.boardType !== type) {
        this.boardType = type;
        this.currentPage = 1;
        this.fetchBoardItems(); // boardType 변경 시 데이터 로드
      }
    },
    async fetchBoardItems() {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await axios.post(
          `http://localhost/board/list/category`,
          {
            params: {
              boardType: this.boardType, // state에서 boardType 사용
              page: this.currentPage,
              size: this.itemsPerPage,
            },
          }
        );
        this.items = response.data.content;
        this.totalPages = response.data.totalPages;
      } catch (error) {
        console.error("게시글 데이터를 가져오는 데 실패했습니다:", error);
        this.error = "게시글 데이터를 가져오는 데 실패했습니다.";
      }
    },

    goToPage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
        this.fetchBoardItems();
      } else {
        console.warn(`Invalid page number: ${page}`);
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchBoardItems();
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchBoardItems();
      }
    },
  },
});
