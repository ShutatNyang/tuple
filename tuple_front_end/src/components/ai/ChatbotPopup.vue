<template>
  <div class="chatbot-container">
    <div class="chatbot-header">
      <h3>AI 추천</h3>
      <span @click="handleClose">▼</span>
    </div>
    <div class="chatbot-body">
      <div class="chat-messages">
        <div
          v-for="(message, index) in messages"
          :key="index"
          :class="['message', message.type]"
        >
          <div class="message-content">
            <span
              v-if="message.type === 'bot'"
              class="message-avatar bot-avatar"
            ></span>
            <span
              v-if="message.type === 'user'"
              class="message-avatar user-avatar"
            ></span>
            <div class="message-text">{{ message.text }}</div>
          </div>
        </div>
      </div>
      <div class="chat-input">
        <input
          v-model="userInput"
          @keyup.enter="sendMessage"
          placeholder="메시지를 입력하세요..."
        />
        <button @click="sendMessage">전송</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import axios from "axios";

const userInput = ref("");
const messages = ref([]);

// 챗봇 환영 메시지 설정
const welcomeMessage =
  "안녕하세요! 여행지 추천 AI입니다. 가고 싶은 여행지나 궁금한 관광지에 대해 물어보세요!";

const emit = defineEmits(["close"]);

onMounted(() => {
  togglePopup();
});

const togglePopup = () => {
  messages.value.push({ text: welcomeMessage, type: "bot" });
};

const handleClose = () => {
  emit("close");
};

const sendMessage = async () => {
  if (userInput.value.trim() === "") return;

  // 사용자 메시지 추가
  messages.value.push({ text: userInput.value, type: "user" });

  // GPT API 호출
  try {
    const response = await axios.post("http://localhost/api/chatbot", {
      message: userInput.value,
    });
    messages.value.push({ text: response.data.reply, type: "bot" });
  } catch (error) {
    console.error("Chatbot API 호출 실패:", error);
    messages.value.push({
      text: "오류가 발생했습니다. 다시 시도해주세요.",
      type: "bot",
    });
  } finally {
    userInput.value = "";

    // 스크롤을 가장 아래로 이동
    setTimeout(() => {
      const chatMessages = document.querySelector(".chat-messages");
      chatMessages.scrollTop = chatMessages.scrollHeight;
    }, 0);
  }
};
</script>

<style scoped>
.chatbot-container {
  position: fixed;
  bottom: 20px;
  left: 20px;
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
  background: #fc6986; /* 분홍색 배경 */
  color: white;
}

.chatbot-icon {
  position: fixed;
  bottom: 20px;
  left: 20px;
  background-color: #fc6986;
  color: #fff;
  border-radius: 50%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  z-index: 1000;
}

.chatbot-header {
  background: #fc6986;
  color: white;
  padding: 15px;
  display: flex;
  cursor: pointer;
  text-align: center;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
}

.chatbot-header h3 {
  margin: 0;
  font-size: 16px;
}

.chatbot-header span {
  font-size: 16px;
}

.chatbot-body {
  display: flex;
  flex-direction: column;
  height: calc(100% - 60px);
  background-color: #f5f5f5;
}

.chat-messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}

.message {
  display: flex;
  margin-bottom: 10px;
}

.message-content {
  display: flex;
  align-items: flex-end;
}

.message.user {
  justify-content: flex-end;
}

.message.bot {
  justify-content: flex-start;
}

.message-avatar {
  width: 30px;
  height: 30px;
  margin: 0 5px;
  font-size: 24px;
}

.message-text {
  max-width: 70%;
  padding: 10px;
  border-radius: 15px;
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  word-wrap: break-word;
}

.message.user .message-text {
  background-color: #dcf8c6;
}

.message.bot .message-text {
  background-color: #fff;
}

.chat-input {
  display: flex;
  padding: 10px;
  background-color: #eee;
  border-top: 1px solid #ccc;
}

.chat-input input {
  flex: 1;
  padding: 10px;
  border: none;
  border-radius: 20px;
  outline: none;
  font-size: 14px;
}

.chat-input button {
  margin-left: 10px;
  padding: 0 15px;
  background-color: #007bff;
  border: none;
  border-radius: 20px;
  color: white;
  font-size: 14px;
  cursor: pointer;
}

.chat-input button:hover {
  background-color: #0056b3;
}
</style>
