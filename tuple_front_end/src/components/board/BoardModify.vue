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
            <svg
              viewBox="0 0 48 48"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <!-- SVG Path 생략 -->
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
          <span class="text-[#8c5f68] text-base font-medium leading-normal"
            >/</span
          >
          <span class="text-[#181112] text-base font-medium leading-normal"
            >게시글 수정</span
          >
        </div>
      </header>
      <div class="px-10 flex flex-1 justify-center">
        <div
          class="layout-content-container flex flex-col w-[512px] max-w-[512px] py-5 max-w-[960px] flex-1"
        >
          <!-- 제목 입력 -->
          <div class="flex max-w-[480px] flex-wrap items-end gap-4 px-4 py-3">
            <label class="flex flex-col min-w-40 flex-1">
              <p
                class="text-[#181112] text-base font-medium leading-normal pb-2"
              >
                Title
              </p>
              <input
                v-model="title"
                placeholder="제목을 입력하세요"
                class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white focus:border-[#e6dbdd] h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal leading-normal"
              />
            </label>
          </div>
          <!-- 에디터 -->
          <div>
            <div class="toolbar flex gap-2 mb-2 px-4">
              <button @click="toggleBold">B</button>
              <button @click="toggleItalic">I</button>
              <select @change="changeFontSize($event)" :value="currentFontSize">
                <option value="12px">12px</option>
                <option value="14px" selected>14px</option>
                <option value="16px">16px</option>
                <option value="18px">18px</option>
                <option value="20px">20px</option>
              </select>
              <input
                type="color"
                @input="changeFontColor"
                :value="currentFontColor"
              />
              <button @click="insertImage">🖼️</button>
            </div>
            <editor-content
              class="editor-content form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl border border-[#e6dbdd] bg-white p-[15px] text-base font-normal leading-normal"
              :editor="editor"
            ></editor-content>
          </div>
          <!-- 버튼 -->
          <div class="flex px-4 py-3 justify-end gap-4">
            <button
              class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-12 px-5 bg-[#fc6986] text-white text-base font-bold leading-normal tracking-[0.015em]"
              @click="modifyPost"
            >
              <span class="truncate">Edit</span>
            </button>
            <button
              class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-12 px-5 bg-[#f5f0f1] text-[#181112] text-base font-bold leading-normal tracking-[0.015em]"
              @click="$router.push(`/board/${boardName}/detail/${id}`)"
            >
              <span class="truncate">Cancel</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Bold from "@tiptap/extension-bold";
import Image from "@tiptap/extension-image";
import Italic from "@tiptap/extension-italic";
import TextStyle from "@tiptap/extension-text-style";
import StarterKit from "@tiptap/starter-kit";
import { Editor, EditorContent } from "@tiptap/vue-3";
import axios from "axios";

const FontSizeAndColor = TextStyle.extend({
  addAttributes() {
    return {
      fontSize: {
        default: null,
        parseHTML: (element) => element.style.fontSize || null,
        renderHTML: (attributes) => {
          if (!attributes.fontSize) return {};
          return { style: `font-size: ${attributes.fontSize}` };
        },
      },
      color: {
        default: null,
        parseHTML: (element) => element.style.color || null,
        renderHTML: (attributes) => {
          if (!attributes.color) return {};
          return { style: `color: ${attributes.color}` };
        },
      },
    };
  },
});

export default {
  props: {
    boardName: {
      type: String,
      required: true,
    },
    id: {
      type: Number,
      required: true,
    },
  },
  components: {
    EditorContent,
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
      return boardNames[this.boardName] || "알 수 없는 게시판";
    },
  },
  data() {
    return {
      title: "",
      memberId: 0,
      currentFontSize: "14px",
      currentFontColor: "#000000",
      editor: null,
      uploadedImages: [],
      previousImageUrls: new Set(),
    };
  },
  created() {
    this.fetchPost();
  },
  mounted() {
    this.editor = new Editor({
      extensions: [StarterKit, Bold, Italic, Image, FontSizeAndColor],
      content: "<p>Loading...</p>",
      onUpdate: ({ editor }) => {
        this.handleEditorUpdate(editor);
      },
    });
  },
  beforeUnmount() {
    if (this.editor) this.editor.destroy();
  },
  methods: {
    async fetchPost() {
      try {
        const response = await axios.post(
          "http://localhost/board/detail",
          null,
          { params: { id: this.id } }
        );
        const { title, content, memberId } = response.data;
        this.title = title;
        this.memberId = memberId;
        this.editor.commands.setContent(content); // 에디터에 데이터 렌더링

        // Extract image URLs from the content and set to previousImageUrls
        const currentImageUrls = this.extractImageUrls(content);
        this.previousImageUrls = currentImageUrls;
      } catch (error) {
        console.error("게시글 데이터를 가져오는 데 실패했습니다:", error);
        alert("게시글을 불러오는 데 실패했습니다.");
      }
    },

    async insertImage() {
      const fileInput = document.createElement("input");
      fileInput.type = "file";
      fileInput.accept = "image/*";
      fileInput.click();

      fileInput.addEventListener("change", async (event) => {
        const files = event.target.files;
        if (files.length > 0) {
          const formData = new FormData();
          Array.from(files).forEach((file) => {
            formData.append("file", file);
            formData.append("associatedTable", "board");
            formData.append("associatedId", this.id); // 게시글 ID로 설정
          });

          try {
            const uploadResponse = await axios.post(
              "http://localhost/file/upload",
              formData,
              {
                headers: {
                  Authorization: `Bearer ${sessionStorage.getItem(
                    "accessToken"
                  )}`,
                  "Content-Type": "multipart/form-data",
                },
              }
            );

            const uploadedFiles = uploadResponse.data;
            uploadedFiles.forEach((file) => {
              const fileUrl = file.fileUrl;
              this.editor
                .chain()
                .focus()
                .insertContent(`<img src="${fileUrl}" alt="uploaded_image" />`)
                .run();
              this.uploadedImages.push({ file, url: fileUrl });
              this.previousImageUrls.add(this.normalizeUrl(fileUrl));
            });
          } catch (error) {
            console.error("이미지 업로드 실패:", error);
            alert("이미지 업로드에 실패했습니다.");
          }
        }
      });
    },

    async modifyPost() {
      try {
        // TipTap 에디터에서 HTML 내용 가져오기
        const content = this.editor.getHTML();

        // 요청 데이터 준비 (BoardUpdateRequestDto 구조와 일치)
        const postData = {
          id: this.id, // 게시글 ID
          title: this.title.trim(), // 제목
          content: content.trim(), // 내용 (HTML 형태)
          memberId: this.memberId, // 로그인된 사용자 ID
        };

        // 게시글 수정 요청
        const response = await axios.post(
          "http://localhost/board/update",
          postData,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            },
          }
        );

        // Handle image deletions
        const currentImageUrls = this.extractImageUrls(content);
        const deletedImages = [...this.previousImageUrls].filter(
          (url) => !currentImageUrls.has(url)
        );

        if (deletedImages.length > 0) {
          try {
            await axios.delete(`http://localhost/file/delete/board`, {
              data: deletedImages, // 배열 형태로 전달
              headers: {
                Authorization: `Bearer ${sessionStorage.getItem(
                  "accessToken"
                )}`,
                "Content-Type": "application/json",
              },
            });
          } catch (error) {
            console.error(
              `이미지 삭제 실패 (${deletedImages.join(", ")}):`,
              error
            );
            alert("일부 이미지를 삭제하는 데 실패했습니다.");
          }
        }

        alert("게시글이 성공적으로 수정되었습니다.");
        this.$router.push(`/board/${this.boardName}/detail/${this.id}`);
      } catch (error) {
        console.error("게시글 수정 중 오류 발생:", error);
        alert("게시글 수정에 실패했습니다.");
      }
    },

    async handleEditorUpdate(editor) {
      const currentContent = editor.getHTML();
      const currentImageUrls = this.extractImageUrls(currentContent);

      // 이전 이미지 목록과 현재 이미지 목록 비교하여 삭제된 이미지 식별
      const deletedImages = [...this.previousImageUrls].filter(
        (url) => !currentImageUrls.has(url)
      );

      if (deletedImages.length > 0) {
        try {
          // 백엔드 deleteFiles API 호출
          await axios.delete(`http://localhost/file/delete/board`, {
            data: { fileUrls: deletedImages }, // 배열 형태로 전달
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
              "Content-Type": "application/json",
            },
          });

          // 삭제된 이미지를 previousImageUrls에서 제거
          deletedImages.forEach((url) => this.previousImageUrls.delete(url));
        } catch (error) {
          console.error(
            `이미지 삭제 실패 (${deletedImages.join(", ")}):`,
            error
          );
          alert("일부 이미지를 삭제하는 데 실패했습니다.");
        }
      }

      // 현재 이미지 목록을 previousImageUrls로 업데이트
      this.previousImageUrls = currentImageUrls;
    },

    /**
     * HTML 콘텐츠에서 이미지 URL을 추출하여 Set으로 반환합니다.
     * @param {string} content - 에디터의 HTML 콘텐츠
     * @returns {Set<string>} - 이미지 URL의 집합
     */
    extractImageUrls(content) {
      const imgUrls = new Set();
      const tempDiv = document.createElement("div");
      tempDiv.innerHTML = content;
      const images = tempDiv.querySelectorAll("img");
      images.forEach((img) => {
        if (img.src) {
          // URL을 표준화 (예: 끝에 슬래시 제거)
          const normalizedUrl = this.normalizeUrl(img.src);
          imgUrls.add(normalizedUrl);
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

    goToBoard() {
      this.$router.push(`/board/${this.boardName}`);
    },

    toggleBold() {
      this.editor.chain().focus().toggleBold().run();
    },
    toggleItalic() {
      this.editor.chain().focus().toggleItalic().run();
    },
    changeFontSize(event) {
      this.currentFontSize = event.target.value;
      this.applyTextStyle();
    },
    changeFontColor(event) {
      this.currentFontColor = event.target.value;
      this.applyTextStyle();
    },
    applyTextStyle() {
      this.editor
        .chain()
        .focus()
        .setMark("textStyle", {
          fontSize: this.currentFontSize,
          color: this.currentFontColor,
        })
        .run();
    },
  },
};
</script>

<style>
.toolbar {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

button {
  padding: 4px 8px;
  border: 1px solid #ccc;
  background-color: #fff;
  cursor: pointer;
  border-radius: 4px;
}

button:hover {
  background-color: #f5f5f5;
}

button:active {
  background-color: #e5e5e5;
}

.editor-content {
  border: 1px solid #ccc;
  padding: 10px;
  height: 500px;
  width: 100%;
  border-radius: 8px;
  background-color: #fff;
}
</style>
