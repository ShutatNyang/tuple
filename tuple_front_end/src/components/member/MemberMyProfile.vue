<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden"
    :style="{
      'font-family': 'Plus Jakarta Sans, Noto Sans, sans-serif',
    }"
  >
    <MemberHeader />
    <div class="layout-container flex h-full grow flex-col">
      <div class="px-40 flex flex-1 justify-center py-1">
        <div
          class="layout-content-container flex flex-col w-[512px] max-w-[512px] py-5 max-w-[960px] flex-1"
        >
          <h3
            class="text-[#111418] tracking-light text-2xl font-bold leading-tight px-4 text-left pb-2 pt-3"
          >
            My Profile
          </h3>

          <!-- ID (email) -->
          <div class="flex max-w-[550px] gap-4 px-4 py-3 w-full">
            <label class="flex flex-col justify-center w-full">
              <p
                class="text-[#181112] text-base font-medium leading-normal pb-2"
              >
                ID (email)
              </p>
              <div class="flex items-center gap-4 w-full justify-between">
                <input
                  v-model="email"
                  type="text"
                  class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal"
                  readonly
                />
                <div class="flex items-center gap-4">
                  <!-- 이미지 미리보기 -->
                  <div class="w-40 h-40 flex items-center justify-center">
                    <img
                      :src="profilePicture || ProfileDefault"
                      alt="Profile Preview"
                      class="w-full h-full object-cover rounded-full border border-gray-300"
                    />
                  </div>
                </div>
              </div>
            </label>
          </div>

          <!-- Name -->
          <div class="flex max-w-[550px] gap-4 px-4 py-3 w-full">
            <label class="flex flex-col w-full">
              <p
                class="text-[#181112] text-base font-medium leading-normal pb-2"
              >
                Name
              </p>
              <input
                v-model="name"
                type="text"
                class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal"
                readonly
              />
            </label>
          </div>

          <!-- Nickname -->
          <div class="flex max-w-[550px] gap-4 px-4 py-3 w-full">
            <label class="flex flex-col w-full">
              <p
                class="text-[#181112] text-base font-medium leading-normal pb-2"
              >
                Nickname
              </p>
              <input
                v-model="nickname"
                type="text"
                class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal"
                readonly
              />
            </label>
          </div>

          <!-- Phone number -->
          <div class="flex max-w-[550px] gap-4 px-4 py-3 w-full">
            <label class="flex flex-col w-full">
              <p
                class="text-[#181112] text-base font-medium leading-normal pb-2"
              >
                Phone Number
              </p>
              <input
                v-model="phone"
                type="text"
                class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal"
                readonly
              />
            </label>
          </div>

          <!-- Age -->
          <div class="flex max-w-[550px] gap-4 px-4 py-3 w-full">
            <label class="flex flex-col w-full">
              <p
                class="text-[#181112] text-base font-medium leading-normal pb-2"
              >
                Age
              </p>
              <input
                v-model="age"
                type="number"
                class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal"
                readonly
              />
            </label>
          </div>

          <!-- Gender -->
          <div class="flex max-w-[550px] gap-4 px-4 py-3 w-full">
            <label class="flex flex-col w-full">
              <p
                class="text-[#181112] text-base font-medium leading-normal pb-2"
              >
                Gender
              </p>
              <input
                v-model="gender"
                type="text"
                class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white h-14 placeholder:text-[#8c5f68] p-[15px] text-base font-normal"
                readonly
              />
            </label>
          </div>

          <!-- Description -->
          <div class="flex max-w-[550px] gap-4 px-4 py-3 w-full">
            <label class="flex flex-col w-full">
              <p
                class="text-[#181112] text-base font-medium leading-normal pb-2"
              >
                Description
              </p>
              <textarea
                v-model="description"
                class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white h-28 placeholder:text-[#8c5f68] p-[15px] text-base font-normal"
                readonly
              ></textarea>
            </label>
          </div>

          <div class="flex flex-col items-center w-full space-y-1 w-[550px]">
            <!-- 수정 버튼 -->
            <div class="flex px-4 py-3">
              <button
                class="flex min-w-[84px] w-[300px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 flex-1 bg-[#fc6986] text-white text-sm font-bold leading-normal tracking-[0.015em]"
                @click.prevent="goProfileModify(myData)"
              >
                <span class="truncate">정보 수정</span>
              </button>
            </div>

            <!-- 비밀번호 재설정 버튼 -->
            <div class="flex px-4 py-3">
              <button
                class="flex min-w-[84px] w-[300px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 flex-1 bg-[#fc6986] text-white text-sm font-bold leading-normal tracking-[0.015em]"
                @click.prevent="goUpdatePassword(myData.email)"
              >
                <span class="truncate">비밀 번호 재설정</span>
              </button>
            </div>

            <!-- 삭제 버튼 -->
            <div class="flex px-4 py-3">
              <button
                class="flex min-w-[84px] w-[300px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 flex-1 bg-[#f5f0f1] text-[#181112] text-sm font-bold leading-normal tracking-[0.015em]"
                @click.prevent="deleteMember"
              >
                <span class="truncate">계정 탈퇴</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProfileDefault from "@/components/icons/Profile_default.png";
import { useAuthStore } from "@/stores/auth";
import axios from "axios";
import MemberHeader from "./MemberHeader.vue";

export default {
  components: {
    MemberHeader,
  },
  data() {
    return {
      ProfileDefault,
      memberId: "",
      name: "",
      profilePicture: null,
      nickname: "",
      phone: "",
      age: null,
      gender: "",
      description: "",
      email: "",
      checkboxTickSvg: `url('data:image/svg+xml,%3csvg viewBox=%270 0 16 16%27 fill=%27rgb(255,255,255)%27 xmlns=%27http://www.w3.org/2000/svg%27%3e%3cpath d=%27M12.207 4.793a1 1 0 010 1.414l-5 5a1 1 0 01-1.414 0l-2-2a1 1 0 011.414-1.414L6.5 9.086l4.293-4.293a1 1 0 011.414 0z%27/%3e%3c/svg%3e')`,
      myData: {},
    };
  },
  mounted() {
    this.initializeMemberInfo();
  },
  methods: {
    // 초기화 메서드
    async initializeMemberInfo() {
      const token = sessionStorage.getItem("accessToken");
      if (!token) {
        alert("로그인이 필요합니다.");
        this.$router.push("/login");
        return;
      }

      try {
        await this.getMemberInfo(token);
      } catch (error) {
        console.error("회원 정보 초기화 실패:", error);
      }
    },

    // 회원 정보 요청
    async getMemberInfo(token) {
      const url = "http://localhost/member/getMemberInfo";

      try {
        const response = await this.authenticatedRequest(url, "post", token);

        // 서버에서 받은 데이터를 상태에 저장
        const data = response.data;
        Object.assign(this, {
          memberId: data.id,
          name: data.name,
          nickname: data.nickname,
          phone: data.phone,
          age: data.age,
          gender: data.gender,
          description: data.description,
          email: data.email,
        });

        await this.getProfileImage(this.memberId);

        // myData 생성
        this.myData = { ...this.$data };
      } catch (error) {
        if (error.response?.status === 401) {
          const newAccessToken = await this.refreshAccessToken();
          if (newAccessToken) {
            await this.getMemberInfo(newAccessToken);
          } else {
            alert("로그인이 필요합니다.");
            this.$router.push("/login");
          }
        } else {
          console.error("회원 정보를 가져오는 데 실패했습니다:", error);
        }
      }
    },

    // Access Token 갱신
    async refreshAccessToken() {
      const url = "http://localhost/member/refresh";
      const refreshToken = sessionStorage.getItem("refreshToken");

      if (!refreshToken) {
        console.error("Refresh Token이 없습니다.");
        return null;
      }

      try {
        const response = await axios.post(
          url,
          {},
          {
            headers: {
              "Content-Type": "application/json",
              refreshToken,
            },
          }
        );

        const newAccessToken = response.data.accessToken;
        sessionStorage.setItem("accessToken", newAccessToken);
        return newAccessToken;
      } catch (error) {
        console.error("Access Token 갱신 중 오류 발생:", error);
        return null;
      }
    },

    // 프로필 이미지 로드
    async getProfileImage(memberId) {
      try {
        const response = await axios.get(
          `http://localhost/file/member/${memberId}`
        );
        if (response.data === "프로필 없음") {
          this.profilePicture = this.ProfileDefault;
        } else {
          const file = response.data[0];
          this.profilePicture = `http://localhost/${file.saveFolder}/${file.saveFile}`;
        }
      } catch (error) {
        console.error("프로필 이미지 로드 실패:", error);
        this.profilePicture = this.ProfileDefault;
      }
    },

    // 프로필 수정 페이지 이동
    goProfileModify(myData) {
      this.$router.push({
        path: "/member/modify",
        query: { myData: JSON.stringify(myData) },
      });
    },

    // 비밀번호 재설정 요청
    async goUpdatePassword(email) {
      if (!email) {
        alert("이메일 정보를 가져올 수 없습니다.");
        return;
      }

      const url = "http://localhost/email/send-reset-password";
      try {
        const response = await axios.post(url, null, {
          params: { email },
        });

        if (response.status === 200) {
          alert("비밀번호 변경 이메일이 전송되었습니다.");
        }
      } catch (error) {
        console.error("비밀번호 재설정 요청 실패:", error);
        alert("비밀번호 재설정 요청 중 문제가 발생했습니다.");
      }
    },

    // 계정 탈퇴
    async deleteMember() {
      const userConfirmed = confirm(
        "정말로 계정을 탈퇴하시겠습니까? 이 작업은 취소할 수 없습니다."
      );
      if (!userConfirmed) return;

      const token = sessionStorage.getItem("accessToken");
      if (!token) {
        alert("로그인이 필요합니다.");
        this.$router.push("/login");
        return;
      }

      const url = "http://localhost/member/delete";
      try {
        const response = await this.authenticatedRequest(url, "post", token, {
          params: { email: this.email },
        });

        if (response.status === 200) {
          alert("회원 탈퇴에 성공했습니다.");
          await this.deleteOldProfilePicture();
          await this.logout(token);
          this.clearSessionAndRedirect();
        }
      } catch (error) {
        alert("회원 탈퇴 중 문제가 발생했습니다.");
        console.error("회원 탈퇴 실패:", error);
      }
    },

    // 기존 프로필 사진 삭제
    async deleteOldProfilePicture() {
      try {
        const deleteUrl = "http://localhost/file/delete/member";
        const payload = { fileUrls: [this.profilePicture] };

        await axios.delete(deleteUrl, { data: payload });
      } catch (error) {
        console.error("프로필 사진 삭제 중 오류 발생:", error);
      }
    },

    // 세션 초기화 및 리다이렉트
    clearSessionAndRedirect() {
      const authStore = useAuthStore();
      authStore.clearLoginUser();

      sessionStorage.removeItem("accessToken");
      sessionStorage.removeItem("refreshToken");

      this.$router.push("/");
    },

    // 인증된 요청을 위한 공통 메서드
    async authenticatedRequest(url, method, token, options = {}) {
      return axios({
        method,
        url,
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        ...options,
      });
    },

    async logout(token) {
      try {
        const logoutUrl = "http://localhost/member/logout";

        await axios.post(
          logoutUrl,
          {},
          { headers: { Authorization: `Bearer ${token}` } }
        );
      } catch (error) {
        console.error("프로필 사진 삭제 중 오류 발생:", error);
      }
    },
  },
};
</script>

<style></style>
