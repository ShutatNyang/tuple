<template>
  <div class="chat-body flex flex-col h-full">
    <!-- 메시지 출력 영역 -->
    <div
      class="messages-container flex-1 overflow-y-auto px-2 py-3"
      @scroll="handleScroll"
      ref="messagesContainer"
    >
      <div
        class="message-item flex items-end gap-2 mb-1"
        v-for="(message, index) in messages"
        :key="index"
        :class="{ 'flex-row-reverse': message.myMessage }"
      >
        <!-- 프로필 이미지 -->
        <div
          class="bg-center bg-no-repeat bg-cover rounded-full w-8 h-8"
          :style="{ backgroundImage: `url(${message.image || defaultAvatar})` }"
        ></div>
        <!-- 메시지 내용 -->
        <div
          class="flex flex-col gap-1 max-w-[70%]"
          :class="{
            'items-end': message.myMessage,
            'items-start': !message.myMessage,
          }"
        >
          <p class="text-xs text-[#b12f49] font-normal">
            {{ message.senderEmail }}
          </p>
          <p
            class="px-3 py-2 rounded-lg text-sm"
            :class="
              message.myMessage
                ? 'bg-[#fc6986] text-[#fbeff1]'
                : 'bg-[#f5dbe0] text-[#20090d]'
            "
          >
            {{ message.message }}
          </p>
        </div>
      </div>
    </div>

    <!-- 입력 영역 -->
    <div
      class="input-container flex items-center px-2 py-2 gap-2 border-t border-gray-200"
    >
      <label class="flex flex-1">
        <input
          placeholder="Type a message..."
          class="form-input w-full resize-none px-3 py-2 rounded-l-lg bg-[#f5dbe0] text-sm text-[#20090d] placeholder:text-[#b12f49] focus:outline-none"
          v-model="newMessage"
          @keyup.enter="sendMessage"
        />
        <button
          class="px-3 py-2 bg-[#fc6986] text-white text-xs font-medium rounded-r-lg"
          @click="sendMessage"
        >
          Send
        </button>
      </label>
    </div>
  </div>
</template>

<script setup>
import { Client } from "@stomp/stompjs";
import axios from "axios";
import { v4 as uuidv4 } from "uuid";
import { onBeforeUnmount, onMounted, ref } from "vue";

const props = defineProps({
  roomId: Number, // 채팅방 ID
});

const messages = ref([]); // 채팅 메시지
const page = ref(1); // 페이지네이션
const hasMoreMessages = ref(true); // 더 불러올 메시지가 있는지 여부
const isFetching = ref(false); // API 호출 상태
const newMessage = ref(""); // 입력 중인 메시지
const userSenderEmail = ref("");
const defaultAvatar =
  "https://cdn.usegalileo.ai/stability/910cb282-4d02-4015-90b3-662223c73c95.png";

const messagesContainer = ref(null); // 메시지 컨테이너 DOM
let stompClient = null; // STOMP 클라이언트

const fetchMessagesUntilScrollable = async () => {
  // 스크롤이 생성될 때까지 반복적으로 메시지를 불러옴
  await fetchMessages(true); // 초기 메시지 로드

  const messagesContainerHeight = messagesContainer.value?.clientHeight || 0;
  const messagesScrollHeight = messagesContainer.value?.scrollHeight || 0;

  // 뷰포트를 가득 채우지 못했다면 추가로 메시지를 로드
  if (
    messagesScrollHeight <= messagesContainerHeight &&
    hasMoreMessages.value
  ) {
    await fetchMessages(false); // 추가 메시지 로드
    fetchMessagesUntilScrollable(); // 재귀 호출
  } else {
    scrollToBottom(); // 스크롤을 하단으로 이동
  }
};

// 이전 메시지 가져오기
const fetchMessages = async (isInitialLoad = false) => {
  if (!hasMoreMessages.value || isFetching.value) return;

  isFetching.value = true;

  try {
    const response = await axios.post(
      `http://localhost/chat/previous-messages`,
      {},
      {
        params: {
          roomId: props.roomId,
          page: page.value,
          size: 6, // 한 페이지에 가져올 메시지 수
        },
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
        },
      }
    );

    const fetchedMessages = response.data.reverse(); // 최신순 정렬

    // 각 메시지의 senderId를 이용하여 프로필 사진 가져오기
    const updatedMessages = await Promise.all(
      fetchedMessages.map(async (message) => {
        try {
          const fileResponse = await axios.get(
            `http://localhost/file/member/${message.senderId}`,
            {
              headers: {
                Authorization: `Bearer ${sessionStorage.getItem(
                  "accessToken"
                )}`,
              },
            }
          );

          const profilePicture =
            fileResponse.data !== "프로필 없음"
              ? `http://localhost/${fileResponse.data[0].saveFolder}/${fileResponse.data[0].saveFile}`
              : defaultAvatar;

          return {
            ...message,
            image: profilePicture, // 프로필 사진 추가
          };
        } catch (error) {
          console.error(
            `프로필 사진 로드 실패 (senderId: ${message.senderId}):`,
            error
          );
          return {
            ...message,
            image: defaultAvatar, // 실패 시 기본 아바타 사용
          };
        }
      })
    );

    messages.value = [...updatedMessages, ...messages.value];
    page.value++;

    if (isInitialLoad) {
      scrollToBottom(); // 초기 로드 시 하단으로 이동
    }
  } catch (error) {
    console.error("이전 메시지 가져오기 오류:", error);
  } finally {
    isFetching.value = false;
  }
};

// 스크롤 핸들러
const handleScroll = () => {
  if (messagesContainer.value.scrollTop === 0) {
    fetchMessages();
  }
};

// STOMP WebSocket 연결
let subscription = null; // 구독 객체

const connectStomp = () => {
  stompClient = new Client({
    brokerURL: "ws://localhost/ws",
    connectHeaders: {
      Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
    },
    onConnect: () => {
      // 메시지 구독 및 ID 저장
      subscription = stompClient.subscribe(
        `/topic/chatroom/${props.roomId}`,
        async (message) => {
          const receivedMessage = JSON.parse(message.body);

          // 받은 메시지가 내 메시지인지 확인
          const isMyMessage = receivedMessage.uniqueId === lastSentMessageId;

          let profilePicture = defaultAvatar; // 기본 이미지로 초기화

          try {
            // senderId로 프로필 이미지 가져오기
            const fileResponse = await axios.get(
              `http://localhost/file/member/${receivedMessage.senderId}`,
              {
                headers: {
                  Authorization: `Bearer ${sessionStorage.getItem(
                    "accessToken"
                  )}`,
                },
              }
            );

            if (fileResponse.data !== "프로필 없음") {
              profilePicture = `http://localhost/${fileResponse.data[0].saveFolder}/${fileResponse.data[0].saveFile}`;
            }
          } catch (error) {
            console.error(
              `프로필 이미지 로드 실패 (senderId: ${receivedMessage.senderId}):`,
              error
            );
          }

          messages.value.push({
            senderEmail: receivedMessage.senderEmail,
            message: receivedMessage.message,
            myMessage: isMyMessage,
            image: profilePicture,
          });
          scrollToBottom();
        }
      );
    },
  });

  stompClient.activate();
};

let lastSentMessageId = null;
// 메시지 전송
const sendMessage = () => {
  if (!newMessage.value.trim()) return;

  const uniqueId = uuidv4();
  // STOMP를 통해 메시지 전송
  stompClient.publish({
    destination: `/app/chat.sendMessage/${props.roomId}`,
    body: JSON.stringify({
      uniqueId: uniqueId,
      message: newMessage.value,
    }),
    headers: {
      Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
    },
  });

  lastSentMessageId = uniqueId;

  newMessage.value = "";
  scrollToBottom();
};

// 스크롤 하단으로 이동
const scrollToBottom = () => {
  setTimeout(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  }, 0);
};

// 컴포넌트 초기화
onMounted(() => {
  fetchMessagesUntilScrollable(); // 초기 로드 시 스크롤 생성
  connectStomp(); // STOMP 연결
});

// 컴포넌트 해제 시 STOMP 연결 해제
onBeforeUnmount(() => {
  if (subscription) {
    subscription.unsubscribe(); // 구독 해제
  }
  if (stompClient) {
    stompClient.deactivate(); // STOMP 클라이언트 비활성화
  }
});
</script>

<style scoped>
.chat-body {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.messages-container {
  flex: 1;
  overflow-y: auto;

  scrollbar-width: thin;
  scrollbar-color: #f5dbe0 transparent;
}

.input-container {
  border-top: 1px solid #ddd;
}

/* 추가적인 스타일이 필요하면 여기에 작성 */
</style>
