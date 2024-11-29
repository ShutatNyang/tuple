<template>
  <div id="app">
    <HeaderNav />
    <RouterView />
    <!-- 라우터 뷰 추가 -->
    <!-- 챗봇 아이콘 -->
    <div class="chat-icon" @click="toggle">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        fill="currentColor"
        viewBox="0 0 256 256"
      >
        <path
          d="M221.8,175.94C216.25,166.38,208,139.33,208,104a80,80,0,1,0-160,0c0,35.34-8.26,62.38-13.81,71.94A16,16,0,0,0,48,200H88.81a40,40,0,0,0,78.38,0H208a16,16,0,0,0,13.8-24.06ZM128,216a24,24,0,0,1-22.62-16h45.24A24,24,0,0,1,128,216ZM48,184c7.7-13.24,16-43.92,16-80a64,64,0,1,1,128,0c0,36.05,8.28,66.73,16,80Z"
        ></path>
      </svg>
    </div>

    <ChatRoom v-if="openChat" @close="toggle" />

    <div class="chatbot-icon" @click="toggleAi">
      <svg
        data-name="Layer 1"
        id="Layer_1"
        width="36"
        height="36"
        viewBox="0 0 36 36"
        xmlns="http://www.w3.org/2000/svg"
      >
        <title />
        <g>
          <path
            class="cls-1"
            d="M26,27H12A11,11,0,0,1,12,5H26A11,11,0,0,1,37,16"
            fill="white"
          />
          <path
            class="cls-1"
            d="M26,23H12A7,7,0,0,1,12,9H26a7,7,0,0,1,7,7"
            fill="white"
          />
          <ellipse
            class="cls-2"
            cx="27.20455"
            cy="15.22653"
            rx="1.29545"
            ry="1.61932"
            fill="white"
          />
          <ellipse
            class="cls-2"
            cx="10.79545"
            cy="15.22653"
            rx="1.29545"
            ry="1.61932"
            fill="white"
          />
          <path
            class="cls-1"
            d="M14.68182,16.17885a5.49719,5.49719,0,0,0,8.63636,0"
            fill="white"
          />
        </g>
        <circle
          class="cls-3"
          cx="37.99999"
          cy="26.87923"
          r="8.99897"
          transform="translate(6.37931 61.03512) rotate(-82.48087)"
          fill="white"
        />
        <line
          class="cls-1"
          x1="33.33333"
          x2="30"
          y1="34.66667"
          y2="40.66667"
          stroke="white"
          stroke-width="2"
        />
        <line
          class="cls-1"
          x1="19"
          x2="19"
          y1="1"
          y2="5"
          stroke="white"
          stroke-width="2"
        />
        <path
          class="cls-1"
          d="M6,40H27.302a3.63357,3.63357,0,0,1,3.69309,3.31178A3.50017,3.50017,0,0,1,27.5,47H6a5,5,0,0,1-5-5V32a5,5,0,0,1,5-5H16"
          fill="white"
        />
        <rect class="cls-1" height="4" width="8" x="8" y="31" fill="white" />
        <line
          class="cls-1"
          x1="24"
          x2="24"
          y1="40"
          y2="47"
          stroke="white"
          stroke-width="2"
        />
      </svg>
    </div>

    <ChatbotPopup v-if="openAi" @close="toggleAi" />
  </div>
</template>

<script>
import HeaderNav from "@/components/main/HeaderNav.vue";

import ChatRoom from "@/components/chat/ChatRoom.vue";
import ChatbotPopup from "./components/ai/ChatbotPopup.vue";

import { Client } from "@stomp/stompjs";

export default {
  name: "App",
  components: {
    HeaderNav,
    ChatRoom,
    ChatbotPopup,
  },
  data() {
    return {
      openChat: false,
      openAi: false,
      stompClient: null,
      isConnected: false,
    };
  },
  methods: {
    toggle() {
      this.openChat = !this.openChat;

      if (this.openChat && !this.isConnected) {
        this.connectWebSocket();
      } else if (!this.openChat && this.isConnected) {
        this.disconnectWebSocket();
      }
    },
    toggleAi() {
      this.openAi = !this.openAi;
    },

    connectWebSocket() {
      this.stompClient = new Client({
        brokerURL: "ws://localhost/ws", // Spring Boot WebSocket 엔드포인트
        connectHeaders: {
          Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
        },
        onConnect: () => {
          this.isConnected = true;
        },
        onDisconnect: () => {
          this.isConnected = false;
        },
        onStompError: (frame) => {
          console.error("STOMP error:", frame);
        },
      });

      this.stompClient.activate();
    },

    disconnectWebSocket() {
      if (this.stompClient) {
        this.stompClient.deactivate();
        this.isConnected = false;
      }
    },
  },
};
</script>

<style>
/* 글로벌 스타일 */
html,
body,
#app {
  margin: 0;
  padding: 0;
  font-family: "Plus Jakarta Sans", "Noto Sans", sans-serif;
  background-color: #fff;
  color: #181013;
  overflow-x: hidden;
}

.chatbot-icon {
  position: fixed;
  bottom: 20px;
  left: 20px;
  background-color: #fc6986;
  color: #fff;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.chat-icon {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: #fc6986;
  color: #fff;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.chatbot-icon:hover {
  background-color: #e05872;
}

.cls-1,
.cls-3 {
  fill: none;
  stroke: #ffffff;
  stroke-width: 2px;
}

.cls-1 {
  stroke-linecap: round;
  stroke-linejoin: round;
}

.cls-2 {
  fill: #ffffff;
}

.cls-3 {
  stroke-miterlimit: 10;
}
</style>
