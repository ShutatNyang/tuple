// src/stores/auth.js
import ProfileDefault from "@/components/icons/Profile_default.png";
import axios from "axios";
import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    accessToken: sessionStorage.getItem("accessToken") || null, // 인증 토큰 정보
    refreshToken: sessionStorage.getItem("refreshToken") || null,
    profilePicture: ProfileDefault, // 기본 프로필 사진
  }),
  actions: {
    async setLoginUser(accessToken, refreshToken) {
      this.accessToken = accessToken; // 로그인 시 accessToken 저장
      this.refreshToken = refreshToken;

      sessionStorage.setItem("accessToken", this.accessToken);
      sessionStorage.setItem("refreshToken", this.refreshToken);

      await this.loadProfilePicture();
    },
    clearLoginUser() {
      this.accessToken = null; // 로그아웃 시 토큰 제거
      this.refreshToken = null;

      sessionStorage.removeItem("accessToken");
      sessionStorage.removeItem("refreshToken");
      this.profilePicture = ProfileDefault; // 추가: 프로필 사진 초기화
    },
    setProfilePicture(newProfilePicture) {
      this.profilePicture = newProfilePicture;
    },

    async initialize() {
      if (this.accessToken) {
        // Access Token이 있을 경우 프로필 정보 로드
        await this.loadProfilePicture();
      }
    },

    async loadProfilePicture() {
      if (!this.accessToken) {
        console.error("Access token 없음");
        this.profilePicture = ProfileDefault;
        return;
      }

      try {
        const response = await axios.get(`http://localhost/file/member`, {
          headers: { Authorization: `Bearer ${this.accessToken}` },
        });

        if (response.data === "프로필 없음") {
          this.setProfilePicture(ProfileDefault);
        } else {
          const file = response.data[0];

          this.setProfilePicture(
            `http://localhost/${file.saveFolder}/${file.saveFile}`
          );
        }
      } catch (error) {
        console.error("프로필 이미지 로드 실패:", error);
        this.setProfilePicture(ProfileDefault);
      }
    },
  },
  getters: {
    // getUserName(state) {
    //   return state.loginUser ? state.loginUser.name : 'Guest'; // 로그인 사용자 이름 반환
    // },
    // isAuthenticated(state) {
    //   return state.isLoggedIn; // 로그인 여부 반환
    // },
  },
});
