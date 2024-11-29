<template>
  <div
    class="flex items-center gap-4 bg-[#fcf8f9] px-4 min-h-[100px] py-2 justify-between"
  >
    <div
      class="border-solid border border-gray-300 flex items-center gap-4 bg-[#FFFFFF] relative"
      style="width: 100%; padding: 16px; border-radius: 8px"
    >
      <div class="bg-[#FFFFFF] flex flex-col justify-center w-full">
        <p
          class="text-[#1d0c12] text-base font-medium leading-normal line-clamp-1 mb-2"
        >
          {{ item.content }}
        </p>
        <p
          class="text-[#a14563] text-sm font-normal leading-normal line-clamp-2 mb-2"
        >
          {{ item.email }} / {{ item.createdTime }}
        </p>
      </div>
      <div class="flex items-center justify-center gap-2 px-3 py-2">
        <div
          class="text-[#8c5f68] cursor-pointer"
          data-icon="Heart"
          data-size="24px"
          data-weight="regular"
          @click="toggleCommentLike(true)"
          :class="{ active: isLiked === 1 }"
        >
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
          :class="{ active: isLiked === 0 }"
          @click="toggleCommentLike(false)"
        >
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
      <!-- Delete 버튼 -->
      <button
        class="delete-btn px-3 py-1 border rounded text-[#d9534f] border-[#f8bbd0] bg-[#fce4ec] hover:bg-[#f8bbd0] hover:text-white absolute bottom-1 right-1delete-btn px-2 py-1 border rounded text-[#d9534f] border-[#f8bbd0] bg-[#fce4ec] hover:bg-[#f8bbd0] hover:text-white absolute bottom-2 right-2 text-xs"
        @click="deleteComment"
      >
        Delete
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "CommentItem",

  props: {
    item: {
      type: Object,
      required: true,
      isLiked: -1,
    },
  },

  methods: {
    async deleteComment() {
      if (confirm("댓글을 삭제하시겠습니까 ?")) {
      }

      const commentDeleteRequestDto = {
        id: this.item.id,
        boardId: this.item.boardId,
        email: this.item.email,
      };

      try {
        await axios.post(
          `http://localhost/comment/delete`,
          commentDeleteRequestDto,
          {
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            },
          }
        );

        // 삭제 성공 시 상위 컴포넌트로 이벤트 전달
        this.$emit("comment-deleted");
      } catch (error) {
        // 상태 코드 확인
        if (error.response && error.response.status === 403) {
          alert("본인의 댓글만 삭제할 수 있습니다.");
        } else {
          console.error("댓글 삭제 중 오류 발생:", error);
          alert("댓글 삭제에 실패했습니다. 잠시 후 다시 시도해 주세요.");
        }
      }
    },

    data() {
      return {
        isLiked: -1,
      };
    },

    async fetchRecommendation() {
      try {
        const response = await axios.post(
          `http://localhost/comment/recommend/status`,
          null,
          {
            params: { commentId: this.item.id },
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

    async toggleCommentLike(like) {
      const targetLikeState = like ? 1 : 0;

      try {
        await this.fetchRecommendation();

        if (this.isLiked === -1) {
          await axios.post(`http://localhost/comment/recommend`, null, {
            params: {
              commentId: this.item.id,
              like: like,
            },
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            },
          });

          this.$emit("submit-like");
        } else if (this.isLiked === targetLikeState) {
          await axios.post(`http://localhost/comment/recommend/cancel`, null, {
            params: {
              commentId: this.item.id,
              like: targetLikeState,
            },
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            },
          });

          this.isLiked = -1;
          this.$emit("submit-like");
        } else {
          alert("좋아요와 싫어요는 동시에 할 수 없습니다.");
        }
      } catch (error) {
        console.error("추천/비추천 처리 중 오류 발생:", error);
        alert("요청 처리 중 문제가 발생했습니다. 다시 시도해주세요.");
      }
    },
  },
};
</script>
