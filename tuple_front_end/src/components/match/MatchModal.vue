<template>
  <div class="chatbot-container">
    <!-- 챗봇 헤더 -->
    <header class="chatbot-header flex items-center">
      <span class="mx-auto">신청자 목록</span>

      <button @click="handleClose" class="chatbot-close-button">
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
    </header>

    <div class="chat-list">
      <div
        class="chat-item"
        v-for="(proposer, index) in proposers"
        :key="index"
      >
        <div class="flex items-center gap-4">
          <!-- 이미지 블록 -->
          <div
            class="chat-avatar"
            :style="{ backgroundImage: `url(${proposer.profileImage})` }"
          ></div>
          <!-- 이메일 -->
          <div class="flex flex-col justify-center chat-details">
            <p class="chat-title">{{ proposer.email }}</p>
          </div>

          <!-- 수락 버튼 -->
          <button
            class="accept-button"
            @click="handleAcceptBtn(proposer.partnerId)"
          >
            수락
          </button>
          <!-- 거절 버튼 -->
          <button
            class="reject-button"
            @click="handleRejectBtn(proposer.partnerId)"
          >
            거절
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import defaultAvatar from "@/components/icons/Profile_Patrick.png";
import axios from "axios";

export default {
  name: "ProposerList",
  props: {
    tourPlanId: {
      type: Number,
      required: true,
    },
    memberId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      proposers: [], // 신청자 목록
    };
  },
  methods: {
    async fetchProposers() {
      try {
        // 매칭 신청자 partner_id 목록 가져오기
        const response = await axios.post(
          `http://localhost/tour-match/list/my-match/${this.tourPlanId}`,
          null,
          {
            params: {
              memberId: this.memberId,
            },
          }
        );

        const partnerIds = response.data; // partner_id 배열

        // partner_id 별 이메일 조회
        const proposerDetails = await Promise.all(
          partnerIds.map(async (partnerId) => {
            try {
              const emailResponse = await axios.get(
                `http://localhost/member/getEmailByMemberId?ownerId=${partnerId.partnerId}`
              );
              const profileResponse = await axios.get(
                `http://localhost/file/member/${partnerId.partnerId}`
              );
              const file = profileResponse.data[0];
              return {
                partnerId: partnerId.partnerId,
                email: emailResponse.data,
                profileImage: `http://localhost/${file.saveFolder}/${file.saveFile}`, // 기본 이미지 설정
              };
            } catch (error) {
              console.error(
                `ID ${partnerId}의 이메일 조회 중 오류 발생:`,
                error
              );
              return {
                email: "정보 없음",
                profileImage: defaultAvatar, // 기본 이미지 설정
              };
            }
          })
        );

        // 데이터 할당
        this.proposers = proposerDetails;
      } catch (error) {
        console.error("신청자 목록 불러오기 중 오류 발생:", error);
      }
    },

    async handleAcceptBtn(partnerId) {
      try {
        await axios.post(`http://localhost/tour-match/respond`, null, {
          params: {
            memberId: this.memberId,
            partnerId: partnerId,
            tourPlanId: this.tourPlanId,
            status: "matched",
          },
        });
        alert("수락되었습니다!");
        this.fetchProposers();
        this.$emit("accepted");
      } catch (error) {
        console.error("거절 중 오류 발생:", error);
      }
    },

    async handleRejectBtn(partnerId) {
      try {
        await axios.post(`http://localhost/tour-match/respond`, null, {
          params: {
            memberId: this.memberId,
            partnerId: partnerId,
            tourPlanId: this.tourPlanId,
            status: "rejected",
          },
        });
        alert("거절되었습니다!");
        this.fetchProposers();
      } catch (error) {
        console.error("거절 중 오류 발생:", error);
      }
    },

    handleClose() {
      this.$emit("close");
    },
  },
  mounted() {
    this.fetchProposers();
  },
};
</script>

<style scoped>
.chatbot-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 350px;
  height: 500px;
  background: #f0dbe2;
  border-radius: 10px;
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
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

.chatbot-close-button {
  position: absolute;
  right: 7px;
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
  background: #f0dbe2;
  padding: 10px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
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
  white-space: nowrap; /* 한 줄로 표시 */
  overflow: hidden; /* 넘치는 텍스트 숨김 */
  text-overflow: ellipsis; /* 넘칠 경우 ... 처리 */
  max-width: 100px;
}

.accept-button {
  background-color: #4caf50;
  color: #ffffff;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 0.7em;
  cursor: pointer;
}

.reject-button {
  background-color: #f44336;
  color: #ffffff;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 0.7em;
  cursor: pointer;
}

/* 마우스 올릴 때 chat-item 효과 제거 */
.chat-item:hover {
  transform: none;
  cursor: default;
}
</style>
