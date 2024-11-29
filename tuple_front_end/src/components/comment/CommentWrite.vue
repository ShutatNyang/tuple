<template>
  <div class="comment-input-container mt-3">
    <textarea
      v-model="newComment"
      class="comment-input w-full px-3 py-2 border rounded-md"
      placeholder="Write a comment..."
    ></textarea>
    <div class="flex justify-end mt-2">
      <button
        class="comment-submit-btn px-4 py-2 bg-[#1d0c12] text-white rounded-md hover:bg-[#3b3b3b] transition"
        @click="submitComment"
      >
        Submit
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "CommentWrite",
  props: {
    id: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      newComment: "", // 댓글 입력 값
    };
  },
  methods: {
    async submitComment() {
      if (!this.newComment.trim()) {
        alert("내용을 입력하세요.");
        return;
      }

      const commentDto = {
        boardId: this.id, // 게시판 ID
        content: this.newComment, // 댓글 내용
      };

      try {
        await axios.post(`http://localhost/comment/insert`, commentDto, {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
          },
        });

        this.$emit("comment-added");

        this.newComment = "";
      } catch (error) {
        console.error("댓글 등록에 실패했습니다:", error);
        alert("댓글 등록에 실패했습니다.");
      }
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

.comment-input {
  border: 1px solid #ddd;
  background-color: #fff;
  border-radius: 8px;
}

.comment-submit-btn {
  cursor: pointer;
}
</style>
