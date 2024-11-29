<template>
  <div class="chatbot-container">
    <!-- 챗봇 헤더 -->
    <header class="chatbot-header">
      <span>{{ activeChatId ? "채팅방" : "채팅 목록" }}</span>
      <button
        v-if="!activeChatId"
        @click="handleClose"
        class="chatbot-close-button"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
        >
          <path
            d="M6.00015 4.58569L12.0002 10.5857L18.0002 4.58569L19.4144 5.99991L13.4144 11.9999L19.4144 17.9999L18.0002 19.4141L12.0002 13.4141L6.00015 19.4141L4.58594 17.9999L10.5859 11.9999L4.58594 5.99991L6.00015 4.58569Z"
            fill="white"
          />
        </svg>
      </button>
      <button v-else @click="handleClose" class="chatbot-close-button">
        <svg
          class="feather feather-chevron-left"
          fill="none"
          height="24"
          stroke="currentColor"
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          viewBox="0 0 24 24"
          width="24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <polyline points="15 18 9 12 15 6" />
        </svg>
      </button>
    </header>
    <!-- 채팅 목록 or 채팅방 -->
    <div class="chat-list" v-if="!activeChatId">
      <div
        class="chat-item"
        v-for="(chat, index) in chats"
        :key="index"
        @click="openChat(chat.id)"
      >
        <div class="flex items-center gap-4">
          <div
            class="chat-avatar"
            :style="{ backgroundImage: `url(${chat.partenrImage})` }"
          ></div>
          <div class="flex flex-col justify-center chat-details">
            <p class="chat-title">{{ chat.partnerEmail }}</p>
          </div>
        </div>
      </div>
    </div>
    <!-- 채팅방 -->
    <Chat v-else :roomId="activeChatId" @back="closeChat" />
  </div>
</template>

<script setup>
import axios from "axios";
import { onMounted, ref } from "vue";
import defaultAvatar from "../../components/icons/Profile_default.png";
import Chat from "./Chat.vue"; // 채팅창 컴포넌트

const chats = ref([]);
const activeChatId = ref(null); // 현재 열려있는 채팅방 ID

const emit = defineEmits(["close"]);

const fetchChatRooms = async () => {
  try {
    const response = await axios.post(
      "http://localhost/chat/my-chat",
      {},
      {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
        },
      }
    );

    const chatsData = response.data;

    // partnerId로 프로필 사진 가져오기
    const updatedChats = await Promise.all(
      chatsData.map(async (chat) => {
        try {
          const fileResponse = await axios.get(
            `http://localhost/file/member/${chat.partnerId}`,
            {
              headers: {
                Authorization: `Bearer ${sessionStorage.getItem(
                  "accessToken"
                )}`,
              },
            }
          );

          // 프로필 사진 URL 설정
          const profilePicture =
            fileResponse.data !== "프로필 없음"
              ? `http://localhost/${fileResponse.data[0].saveFolder}/${fileResponse.data[0].saveFile}`
              : defaultAvatar;

          return {
            ...chat,
            partenrImage: profilePicture,
          };
        } catch (error) {
          console.error(
            `프로필 사진 로드 실패 (partnerId: ${chat.partnerId}):`,
            error
          );
          return {
            ...chat,
            partenrImage: defaultAvatar,
          };
        }
      })
    );

    chats.value = updatedChats;
  } catch (error) {
    console.error("채팅방 정보를 가져오는데 실패했습니다:", error);
  }
};

// 채팅방 열기
const openChat = (chatId) => {
  activeChatId.value = chatId;
};

// 채팅방 닫기 (목록으로 돌아가기)
const closeChat = () => {
  activeChatId.value = null;
};

// 모달 닫기
const handleClose = () => {
  if (activeChatId.value) {
    closeChat();
  } else {
    // 부모 컴포넌트로 이벤트 전송
    emit("close");
  }
};

onMounted(() => {
  fetchChatRooms();
});
</script>

<style scoped>
.chatbot-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 350px;
  height: 500px;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  overflow: auto;
  z-index: 1000;
}

.chatbot-header {
  background: #fc6986;
  color: #ffffff;
  padding: 5px 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
}

.chatbot-close-button {
  background: none;
  border: none;
  color: #ffffff;
  cursor: pointer;
  font-size: 1.2em;
}

.chat-list {
  overflow-y: auto;
  flex: 1;
}

.chat-item {
  background: #ffffff;
  padding: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.chat-item:hover {
  transform: translateY(-2px);
}

.chat-avatar {
  background-position: center;
  background-size: cover;
  border-radius: 50%;
  width: 56px;
  height: 56px;
}

.chat-details {
  flex: 1;
}

.chat-title {
  color: #20090d;
  font-size: 1em;
  font-weight: 500;
  margin: 0;
}
</style>
