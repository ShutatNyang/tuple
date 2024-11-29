<template>
  <div class="comment-container px-4 py-2">
    <div class="comment-container px-4 py-2">
      <div
        class="flex items-center gap-2 text-[#000000]"
        data-icon="PaperPlaneTilt"
        data-size="24px"
        data-weight="regular"
      >
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
        <h3 class="text-[#1d0c12] text-lg font-bold leading-normal">
          Comments
        </h3>
      </div>
    </div>

    <div>
      <CommentItem
        v-for="(comment, index) in comments"
        :key="index"
        :item="comment"
        @comment-deleted="$emit('comment-deleted')"
        @submit-like="$emit('submit-like')"
        @cancel-like="$emit('cancel-like')"
      />
    </div>

    <div v-if="hasMoreComments" class="flex justify-center">
      <button
        @click="loadMoreComments"
        class="bg-[#fc6986] text-white px-4 py-2 rounded-md shadow hover:bg-[#f5f0f1] transition"
      >
        ...더보기
      </button>
    </div>

    <div>
      <CommentWrite :id="id" @comment-added="$emit('comment-added')" />
    </div>
  </div>
</template>

<script>
import CommentItem from "./item/CommentItem.vue";
import CommentWrite from "./CommentWrite.vue";

export default {
  name: "Comment",
  components: {
    CommentItem,
    CommentWrite,
  },
  props: {
    comments: {
      type: Object,
      required: true,
    },
    id: {
      type: String,
      required: true,
    },
    hasMoreComments: {
      type: Boolean, // 더보기 버튼 표시 여부
      default: true,
    },
  },
  methods: {
    handleToggleLike(commentId, like) {
      // 댓글 좋아요/싫어요 이벤트를 BoardDetailView로 전달
      this.$emit("toggle-like", commentId, like);
    },
    handleDeleteComment(commentId) {
      // 댓글 삭제 이벤트를 BoardDetailView로 전달
      this.$emit("delete-comment", commentId);
    },
    loadMoreComments() {
      // 더보기 버튼 클릭 이벤트를 부모로 emit
      this.$emit("load-more-comments");
    },
  },
};
</script>

<style scoped>
.comment-container {
  background-color: #fcf8f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
