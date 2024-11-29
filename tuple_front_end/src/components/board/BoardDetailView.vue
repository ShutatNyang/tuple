<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden"
    style="font-family: 'Plus Jakarta Sans', 'Noto Sans', sans-serif"
  >
    <div class="layout-container flex h-full grow flex-col">
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
            게시판
          </h2>
        </div>
        <div class="flex flex-wrap gap-2 p-4">
          <a
            class="text-[#8c5f68] text-base font-medium leading-normal"
            href="#"
            @click.prevent="goToBoard"
            >{{ localizedBoardName }}</a
          >
        </div>
      </header>
      <div class="px-20 flex flex-1 justify-center py-5">
        <div
          class="layout-content-container flex flex-col max-w-[960px] flex-1"
        >
          <h1
            class="text-[#181112] text-[22px] font-bold leading-tight tracking-[-0.015em] px-4 text-left pb-3 pt-5"
          >
            {{ item.title }}
          </h1>
          <p
            class="text-[#8c5f68] text-sm font-normal leading-normal pb-3 pt-1 px-4 underline"
          >
            By: {{ item.email }}
          </p>
          <p
            class="text-[#8c5f68] text-sm font-normal leading-normal pb-3 pt-1 px-4"
          >
            Published on {{ item.registTime }}
          </p>
          <p
            class="text-[#181112] text-base font-normal pb-3 pt-1 px-4"
            v-html="item.content"
          ></p>
          <div class="flex flex-wrap gap-4 px-4 py-2">
            <div class="flex items-center justify-center gap-2 px-3 py-2">
              <div
                class="text-[#8c5f68] cursor-pointer"
                data-icon="Heart"
                data-size="24px"
                data-weight="regular"
                @click="toggleLike(true)"
                :class="{ active: isLiked === 1 }"
              >
                <!-- 좋아요 SVG -->
                <svg
                  viewBox="0 0 512 512"
                  xmlns="http://www.w3.org/2000/svg"
                  width="24px"
                  height="24px"
                  fill="currentColor"
                >
                  <g data-name="1" id="_1">
                    <path
                      d="M348.45,432.7H261.8a141.5,141.5,0,0,1-49.52-8.9l-67.5-25.07a15,15,0,0,1,10.45-28.12l67.49,25.07a111.79,111.79,0,0,0,39.08,7h86.65a14.21,14.21,0,1,0,0-28.42,15,15,0,0,1,0-30H368.9a14.21,14.21,0,1,0,0-28.42,15,15,0,0,1,0-30h20.44a14.21,14.21,0,0,0,10.05-24.26,14.08,14.08,0,0,0-10.05-4.16,15,15,0,0,1,0-30h20.45a14.21,14.21,0,0,0,10-24.26,14.09,14.09,0,0,0-10-4.17H268.15A15,15,0,0,1,255,176.74a100.2,100.2,0,0,0,9.2-29.33c3.39-21.87-.79-41.64-12.42-58.76a12.28,12.28,0,0,0-22.33,7c.49,51.38-23.25,88.72-68.65,108a15,15,0,1,1-11.72-27.61c18.72-8,32.36-19.75,40.55-35.08,6.68-12.51,10-27.65,9.83-45C199.31,77,211,61,229.18,55.34s36.81.78,47.45,16.46c24.71,36.36,20.25,74.1,13.48,97.21H409.79a44.21,44.21,0,0,1,19.59,83.84,44.27,44.27,0,0,1-20.44,58.42,44.27,44.27,0,0,1-20.45,58.43,44.23,44.23,0,0,1-40,63Z"
                    />
                    <path
                      d="M155,410.49H69.13a15,15,0,0,1-15-15V189.86a15,15,0,0,1,15-15H155a15,15,0,0,1,15,15V395.49A15,15,0,0,1,155,410.49Zm-70.84-30H140V204.86H84.13Z"
                    />
                  </g>
                </svg>
              </div>
              <p
                class="text-[#8c5f68] text-[13px] font-bold leading-normal tracking-[0.015em]"
              >
                {{ item.likeCount }}
              </p>
            </div>
            <div class="flex items-center justify-center gap-2 px-3 py-2">
              <div
                class="text-[#8c5f68] cursor-pointer"
                data-icon="ChatCircle"
                data-size="24px"
                data-weight="regular"
                @click="toggleLike(false)"
                :class="{ active: isLiked === 0 }"
              >
                <!-- 싫어요 SVG -->
                <svg
                  viewBox="0 0 512 512"
                  xmlns="http://www.w3.org/2000/svg"
                  width="24px"
                  height="24px"
                  fill="currentColor"
                >
                  <g data-name="1" id="_1">
                    <path
                      d="M242.28,427.39a43.85,43.85,0,0,1-13.1-2c-18.22-5.69-29.87-21.64-29.69-40.62.16-17.35-3.15-32.5-9.83-45-8.19-15.33-21.83-27.13-40.55-35.08A15,15,0,1,1,160.83,277c45.4,19.26,69.14,56.6,68.65,108a12.28,12.28,0,0,0,22.33,7c28.34-41.71,3.47-87.63,3.22-88.09a15,15,0,0,1,13.12-22.27H409.79a14.22,14.22,0,0,0,0-28.43H389.34a15,15,0,1,1,0-30,14.2,14.2,0,0,0,14.21-14.21,14.23,14.23,0,0,0-14.21-14.21H368.9a15,15,0,0,1,0-30,14.21,14.21,0,1,0,0-28.42H348.45a15,15,0,0,1,0-30,14.21,14.21,0,1,0,0-28.42H261.8a111.69,111.69,0,0,0-39.07,7l-67.5,25.07A15,15,0,0,1,144.78,82l67.5-25.07A141.5,141.5,0,0,1,261.8,48h86.65a44.25,44.25,0,0,1,40,63,44.27,44.27,0,0,1,20.45,58.43,44.27,44.27,0,0,1,20.44,58.42,44.21,44.21,0,0,1-19.59,83.84H290.11c6.77,23.11,11.23,60.85-13.48,97.22A41.21,41.21,0,0,1,242.28,427.39Z"
                    />
                    <path
                      d="M155,305.85H69.13a15,15,0,0,1-15-15V85.21a15,15,0,0,1,15-15H155a15,15,0,0,1,15,15V290.85A15,15,0,0,1,155,305.85Zm-70.84-30H140V100.21H84.13Z"
                    />
                  </g>
                </svg>
              </div>
              <p
                class="text-[#8c5f68] text-[13px] font-bold leading-normal tracking-[0.015em]"
              >
                {{ item.dislikeCount }}
              </p>
            </div>
            <div class="flex items-center justify-center gap-2 px-3 py-2">
              <div
                class="text-[#8c5f68]"
                data-icon="PaperPlaneTilt"
                data-size="24px"
                data-weight="regular"
              >
                <!-- 댓글 수 SVG -->
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24px"
                  height="24px"
                  fill="currentColor"
                  viewBox="0 0 256 256"
                >
                  <path
                    d="M128,24A104,104,0,0,0,36.18,176.88L24.83,210.93a16,16,0,0,0,20.24,20.24l34.05-11.35A104,104,0,1,0,128,24Zm0,192a87.87,87.87,0,0,1-44.06-11.81,8,8,0,0,0-6.54-.67L40,216,52.47,178.6a8,8,0,0,0-.66-6.54A88,88,0,1,1,128,216Z"
                  ></path>
                </svg>
              </div>
              <p
                class="text-[#8c5f68] text-[13px] font-bold leading-normal tracking-[0.015em]"
              >
                {{ commentCount }}
              </p>
            </div>
          </div>
          <div class="flex px-4 py-3 justify-end gap-4">
            <button
              class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-12 px-5 bg-[#fc6986] text-white text-base font-bold leading-normal tracking-[0.015em]"
              @click="$router.push(`/board/${boardName}/modify/${item.id}`)"
            >
              <span class="truncate">Edit Post</span>
            </button>
            <button
              class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-12 px-5 bg-[#f5f0f1] text-[#181112] text-base font-bold leading-normal tracking-[0.015em]"
              @click="deletePost"
            >
              <span class="truncate">Delete Post</span>
            </button>
          </div>
          <CommentList
            :comments="comments"
            :id="id"
            :has-more-comments="hasMoreComments"
            @comment-added="fetchComments"
            @comment-deleted="fetchComments"
            @submit-like="fetchComments"
            @cancel-like="fetchComments"
            @load-more-comments="handlePageNav"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

import CommentList from "../comment/CommentList.vue";

export default {
  components: {
    CommentList,
  },
  props: {
    boardName: {
      type: String,
      required: true,
    },
    id: {
      type: String,
      required: true,
    },
  },
  computed: {
    localizedBoardName() {
      const boardNames = {
        notice: "공지사항",
        free: "자유게시판",
        event: "이벤트",
        review: "여행 후기",
        qna: "QnA",
      };
      return boardNames[this.boardName] || "알 수 없는 게시판"; // 기본값 설정
    },
  },
  data() {
    return {
      item: {},
      isLiked: -1,
      comments: [], // API로부터 받아올 댓글 데이터
      currentPage: 1, // 현재 페이지
      pageSize: 5, // 한 페이지의 댓글 개수
      commentCount: 0,
    };
  },
  async created() {
    await this.fetchBoardItem();
    this.fetchComments(); // 컴포넌트가 마운트될 때 댓글 데이터를 가져옴
    this.fetchCommentCount();
  },
  methods: {
    goToBoard() {
      this.$router.push(`/board/${this.boardName}`); // 동적 경로로 이동
    },

    async fetchBoardItem() {
      try {
        const response = await axios.post(
          `http://localhost/board/detail?id=${this.id}`
        );
        this.item = response.data;
      } catch (error) {
        console.error("게시글 데이터를 가져오는 데 실패했습니다:", error);
      }
    },

    async fetchUserRecommendation() {
      try {
        const response = await axios.post(
          `http://localhost/board/recommend/status`,
          null,
          {
            params: { boardId: this.id },
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            },
          }
        );
        this.isLiked = response.data;
      } catch (error) {
        console.error("추천 상태를 가져오는 데 실패했습니다:", error);
      }
    },

    async toggleLike(like) {
      const targetLikeState = like ? 1 : 0;

      try {
        await this.fetchUserRecommendation();

        if (this.isLiked === -1) {
          await axios.post(`http://localhost/board/recommend`, null, {
            params: {
              boardId: this.id,
              like: like,
            },
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            },
          });

          await this.fetchBoardItem();
        } else if (this.isLiked === targetLikeState) {
          await axios.post(`http://localhost/board/recommend/cancel`, null, {
            params: {
              boardId: this.id,
              like: targetLikeState,
            },
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            },
          });

          this.isLiked = -1;
          await this.fetchBoardItem();
        } else {
          alert("좋아요와 싫어요는 동시에 할 수 없습니다.");
        }
      } catch (error) {
        console.error("추천/비추천 처리 중 오류 발생:", error);
        alert("요청 처리 중 문제가 발생했습니다. 다시 시도해주세요.");
      }
    },

    /**
     * 게시글 삭제 시, 모든 연관 이미지 파일을 삭제하고 게시글을 삭제합니다.
     */
    async deletePost() {
      try {
        // 게시글의 콘텐츠에서 이미지 URL 추출
        const content = this.item.content;
        const imageUrls = this.extractImageUrls(content);

        // 이미지가 존재하면 이미지 삭제 API 호출
        if (imageUrls.length > 0) {
          await axios.delete(`http://localhost/file/delete/board`, {
            data: { fileUrls: imageUrls }, // 배열 형태로 전달
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
              "Content-Type": "application/json",
            },
          });
        }

        // 게시글 삭제 API 호출
        await axios.post(`http://localhost/board/delete`, null, {
          params: {
            id: this.id,
          },
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
          },
        });
        alert("글 삭제가 완료되었습니다!");
        this.goToBoard(); // 삭제 후 게시판으로 리디렉션
      } catch (error) {
        console.error("글 삭제 중 오류가 발생했습니다:", error);
        alert("글 삭제에 실패했습니다. 다시 시도해주세요.");
      }
    },

    /**
     * 게시글 콘텐츠에서 모든 이미지 URL을 추출하여 배열로 반환합니다.
     * @param {string} content - 게시글의 HTML 콘텐츠
     * @returns {Array<string>} - 이미지 URL의 배열
     */
    extractImageUrls(content) {
      const imgUrls = [];
      const tempDiv = document.createElement("div");
      tempDiv.innerHTML = content;
      const images = tempDiv.querySelectorAll("img");
      images.forEach((img) => {
        if (img.src) {
          // URL을 표준화 (예: 끝에 슬래시 제거)
          const normalizedUrl = this.normalizeUrl(img.src);
          imgUrls.push(normalizedUrl);
        }
      });
      return imgUrls;
    },

    /**
     * URL을 표준화하는 메서드
     * @param {string} url - 표준화할 URL
     * @returns {string} - 표준화된 URL
     */
    normalizeUrl(url) {
      return url.endsWith("/") ? url.slice(0, -1) : url;
    },

    async fetchComments() {
      try {
        const response = await axios.post(
          `http://localhost/comment/${this.id}`, // API URL에 게시글 ID 사용
          {}, // POST 요청에 필요한 데이터가 없으면 빈 객체
          {
            params: {
              page: this.currentPage, // 현재 페이지
              size: this.pageSize, // 한 페이지 크기
            },
            headers: {
              "Content-Type": "application/json", // 헤더 설정
            },
          }
        );
        this.comments = response.data; // API 응답 데이터를 comments 배열에 저장
        this.fetchCommentCount();
      } catch (error) {
        console.error("댓글 데이터를 불러오는 중 오류 발생:", error);
      }
    },

    async fetchCommentCount() {
      try {
        const response = await axios.post(
          `http://localhost/comment/count`,
          {},
          {
            params: {
              boardId: this.id,
            },
            headers: {
              "Content-Type": "application/json", // 헤더 설정
            },
          }
        );
        this.commentCount = response.data;
        this.hasMoreComments =
          this.commentCount > this.currentPage * this.pageSize ? true : false;
      } catch (error) {}
    },

    handlePageNav() {
      // 현재 페이지 증가
      this.currentPage += 1;
      // 새로운 댓글 데이터를 가져옴
      this.fetchComments();
      this.fetchCommentCount();
    },
  },
};
</script>

<style scoped>
.cursor-pointer {
  cursor: pointer; /* 클릭 가능 표시 */
}
</style>
