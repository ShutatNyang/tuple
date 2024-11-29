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

          <!-- 웹 에디터 (내용 입력) -->
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
            <div
              class="flex items-center px-4 py-3 gap-3 @container h-full max-h-[520px]"
            >
              <editor-content
                class="editor-content form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl border border-[#e6dbdd] bg-white p-[15px] text-base font-normal leading-normal"
                :editor="editor"
              ></editor-content>
            </div>
          </div>

          <!-- 게시 버튼 -->
          <div class="flex px-4 py-3 justify-end">
            <button
              class="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-12 px-5 bg-[#fc6986] text-white text-base font-bold leading-normal tracking-[0.015em]"
              @click="insertPost"
            >
              <span class="truncate">Publish</span>
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
  components: {
    EditorContent,
  },
  props: {
    boardName: {
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
      title: "",
      editor: null,
      currentFontSize: "14px",
      currentFontColor: "#000000",
      uploadedImages: [], // 업로드된 이미지 정보를 { file, url } 형태로 저장
      previousImageUrls: new Set(), // 이전에 업로드된 이미지 URL 저장
      associatedId: 0, // 게시글 ID 추적 (0은 아직 등록되지 않음을 의미)
    };
  },
  mounted() {
    this.editor = new Editor({
      extensions: [StarterKit, Bold, Italic, Image, FontSizeAndColor],
      content: "<p>여기에 내용을 입력하세요...</p>",
      onUpdate: ({ editor }) => {
        this.handleEditorUpdate(editor);
      },
    });
  },
  beforeUnmount() {
    if (this.editor) {
      this.editor.destroy();
    }
  },
  methods: {
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
            formData.append("associatedId", 0); // 게시글 ID는 아직 없으므로 0으로 설정
          });

          try {
            // 이미지 서버 업로드 호출
            const uploadResponse = await axios.post(
              "http://localhost/file/upload", // 포트 제거
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

            const uploadedFiles = uploadResponse.data; // 업로드된 파일 정보
            uploadedFiles.forEach((file) => {
              const fileUrl = file.fileUrl;
              // 서버 URL을 에디터에 삽입
              this.editor
                .chain()
                .focus()
                .insertContent(`<img src="${fileUrl}" alt="uploaded_image" />`)
                .run();

              // 업로드된 이미지를 저장
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

    async insertPost() {
      try {
        // 에디터에서 HTML 내용을 가져옵니다
        let content = this.editor.getHTML();

        // 게시글 데이터를 준비합니다
        const postData = {
          title: this.title,
          content,
          boardType: this.boardName,
        };

        // 게시글을 서버에 등록합니다
        const response = await axios.post(
          "http://localhost/board/insert", // 포트 제거
          postData,
          {
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            },
          }
        );

        const postId = response.data; // 등록된 게시글 ID
        this.associatedId = postId; // 게시글 ID 저장

        const currentContent = this.editor.getHTML();
        const currentImageUrls = this.extractImageUrls(currentContent);

        if (currentImageUrls.length > 0) {
          // 게시글 ID와 이미지를 연관짓는 업데이트 요청
          await axios.post("http://localhost/file/updateAssociatedId", {
            associatedTable: "board",
            associatedId: postId,
          });
        }

        alert("게시글이 성공적으로 등록되었습니다.");

        // 게시글 등록 후, 에디터 내용을 초기화하지 않고 이전 이미지 URL을 갱신
        // this.uploadedImages = []; // 업로드 이미지 초기화 (필요 시)
        // 이전 이미지 URL을 현재 에디터의 이미지 URL로 갱신
        // const currentImageUrls = this.extractImageUrls(this.editor.getHTML());
        // this.previousImageUrls = currentImageUrls;

        // 에디터 내용 초기화는 선택 사항으로, 필요 시 아래 주석을 해제하세요.
        // this.editor.commands.setContent("<p>여기에 내용을 입력하세요...</p>");

        // 게시글 목록으로 이동
        this.$router.push(`/board/${this.boardName}`);
      } catch (error) {
        console.error("게시글 등록 실패:", error);
        alert("게시글 등록에 실패했습니다.");
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
            data: { fileUrls: deletedImages }, // 삭제할 파일 URL 목록 전달
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
  height: 500px; /* 높이를 500px로 조정 */
  width: 100%; /* 너비를 부모 요소에 맞게 설정 */

  /* width: 1000px; */ /* 기존에 설정된 너비를 주석 처리하여 Tailwind CSS에 맞게 조정 */
  border-radius: 8px; /* 테두리 둥글게 */
  background-color: #fff;
}

.ProseMirror {
  min-height: 100%; /* 높이를 충분히 키움 */
  width: 100%; /* 너비를 부모 요소에 맞게 설정 */
  max-width: 1000px; /* 최대 너비 제한 */
  margin: 0 auto; /* 중앙 정렬 */
  padding: 10px;
  background-color: #fff; /* 배경색 설정 */
  border: 1px solid #ccc; /* 테두리 설정 */
  border-radius: 8px; /* 테두리 둥글게 */
  box-sizing: border-box; /* 패딩 포함 크기 계산 */
  overflow-y: auto; /* 세로 스크롤 활성화 */
}

.toolbar button:hover,
.toolbar select:hover,
.toolbar input[type="color"] {
  padding: 4px 8px;
  margin-right: 4px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}
</style>
