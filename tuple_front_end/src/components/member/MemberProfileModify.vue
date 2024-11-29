<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden"
    :style="{
      'font-family': 'Plus Jakarta Sans, Noto Sans, sans-serif',
    }"
  >
    <MemberHeader />
    <div class="layout-container flex h-full grow flex-col">
      <div class="px-20 flex flex-1 justify-center items-center">
        <div
          class="layout-content-container flex flex-col justify-center items-center w-[960px] max-w-[960px] py-5 max-w-[960px]"
        >
          <h3
            class="text-[#111418] tracking-light text-2xl font-bold leading-tight px-4 text-center pb-2 pt-3"
          >
            Create an account
          </h3>
          <form @submit.prevent="modifyProfile">
            <!-- 가운데 정렬 -->
            <div class="flex flex-col items-center w-full space-y-4 w-[800px]">
              <!-- 이름 입력 -->
              <div class="flex flex-col items-center gap-4 px-4 py-3 w-[550px]">
                <label class="flex flex-col min-w-40 w-full">
                  <p
                    class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
                  >
                    Name
                  </p>
                  <input
                    class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border-none bg-[#f4e6e9] h-14 placeholder:text-[#9e4759] p-4 text-base font-normal"
                    v-model="name"
                    placeholder="Full Name"
                  />
                </label>
              </div>

              <!-- 프로필 사진 입력 -->
              <div
                class="flex flex-col items-center max-w-[550px] gap-4 px-4 py-3 w-full"
              >
                <label class="flex flex-col min-w-40 w-full">
                  <p
                    class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
                  >
                    Profile Picture
                  </p>
                  <div class="flex items-center gap-4 w-full justify-between">
                    <!-- 파일 업로드 버튼 -->
                    <div
                      class="relative w-full flex justify-center items-center max-w-[300px]"
                    >
                      <input
                        type="file"
                        class="absolute inset-0 opacity-0 w-full h-full cursor-pointer"
                        @change="handleFileUpload"
                        accept="image/*"
                      />
                      <div
                        class="bg-[#f4e6e9] text-[#9e4759] flex items-center justify-center h-14 w-full rounded-xl text-m font-large"
                      >
                        Click to select a file
                      </div>
                    </div>

                    <!-- 이미지 미리보기 -->
                    <div class="w-40 h-40 flex items-center justify-center">
                      <img
                        :src="previewUrl || ProfileDefault"
                        alt="Profile Preview"
                        class="w-full h-full object-cover rounded-full border border-gray-300"
                      />
                    </div>
                  </div>
                </label>
              </div>

              <!-- 닉네임 입력 -->
              <div
                class="flex flex-col items-center max-w-[550px] gap-4 px-4 py-3 w-full"
              >
                <label class="flex flex-col min-w-40 w-full">
                  <p
                    class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
                  >
                    Nickname
                  </p>
                  <input
                    class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border-none bg-[#f4e6e9] h-14 placeholder:text-[#9e4759] p-4 text-base font-normal"
                    v-model="nickname"
                    placeholder="Nickname"
                  />
                </label>
              </div>

              <!-- 전화번호 입력 -->
              <div
                class="flex flex-col items-center max-w-[550px] gap-4 px-4 py-3 w-full"
              >
                <label class="flex flex-col min-w-40 w-full">
                  <p
                    class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
                  >
                    Phone number
                  </p>
                  <input
                    class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border-none bg-[#f4e6e9] h-14 placeholder:text-[#9e4759] p-4 text-base font-normal"
                    v-model="phone"
                    placeholder="Phone number"
                  />
                </label>
              </div>

              <!-- 나이 입력 -->
              <div
                class="flex max-w-[550px] flex-wrap items-end gap-4 px-4 py-3 w-full"
              >
                <label class="flex flex-col min-w-40 flex-1">
                  <p
                    class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
                  >
                    Age
                  </p>
                  <input
                    type="number"
                    class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border-none bg-[#f4e6e9] focus:border-none h-14 placeholder:text-[#9e4759] p-4 text-base font-normal leading-normal"
                    v-model="age"
                    placeholder="Enter your age [min: 0, max: 120]"
                    min="0"
                    max="120"
                  />
                </label>
              </div>

              <!-- 성별 선택 -->
              <div
                class="flex max-w-[550px] flex-wrap items-end gap-4 px-4 py-3 w-full"
              >
                <label class="flex flex-col min-w-40 flex-1">
                  <p
                    class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
                  >
                    Gender
                  </p>
                  <select
                    class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border-none bg-[#f4e6e9] focus:border-none h-14 placeholder:text-[#9e4759] p-4 text-base font-normal leading-normal"
                    v-model="gender"
                  >
                    <option value="">Select your gender</option>
                    <option
                      v-for="(option, index) in genderOptions"
                      :key="index"
                      :value="option"
                    >
                      {{ option }}
                    </option>
                  </select>
                </label>
              </div>

              <!-- 자기 소개 -->
              <div
                class="flex max-w-[550px] flex-wrap items-end gap-4 px-4 py-3 w-full"
              >
                <label class="flex flex-col min-w-40 flex-1">
                  <p
                    class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
                  >
                    자기 소개
                  </p>
                  <textarea
                    class="form-input flex w-full min-w-0 flex-1 resize-none rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border-none bg-[#f4e6e9] focus:border-none h-28 placeholder:text-[#9e4759] p-4 text-base font-normal leading-normal"
                    v-model="description"
                    placeholder="자기 소개(선택)"
                  ></textarea>
                </label>
              </div>

              <!-- 제출 버튼 -->
              <div class="flex items-center justify-center w-full px-4 py-3">
                <button
                  type="submit"
                  class="w-[300px] bg-[#9e4759] hover:bg-[#8d2c3d] text-white font-medium rounded-xl h-14 text-xl"
                >
                  Edit My Profile
                </button>
              </div>
            </div>
          </form>
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
      genderOptions: ["Male", "Female", "Other"],
      previewUrl: "",
      profileImage: null,
    };
  },
  mounted() {
    this.loadUserProfile();
  },
  methods: {
    // 사용자 프로필 데이터를 로드하는 메서드
    loadUserProfile() {
      const queryData = this.$route.query.myData;
      if (queryData) {
        try {
          const myData = JSON.parse(queryData);
          Object.assign(this, {
            memberId: myData.memberId,
            name: myData.name,
            previewUrl: myData.profilePicture,
            profilePicture: myData.profilePicture,
            nickname: myData.nickname,
            phone: myData.phone,
            age: myData.age,
            gender: this.formatGender(myData.gender),
            description: myData.description,
            email: myData.email,
          });
        } catch (error) {
          console.error("Error parsing myData:", error);
        }
      }
    },

    // Gender 포맷 변환 메서드
    formatGender(gender) {
      return gender
        ? gender.charAt(0).toUpperCase() + gender.slice(1).toLowerCase()
        : "";
    },

    // 프로필 수정 메서드
    async modifyProfile() {
      if (!sessionStorage.getItem("accessToken")) {
        console.error("No access token found in sessionStorage");
        return;
      }

      const payload = {
        name: this.name,
        nickname: this.nickname,
        phone: this.phone,
        age: this.age,
        gender: this.gender,
        description: this.description,
        email: this.email,
      };

      try {
        const response = await axios.post(
          "http://localhost/member/modify",
          payload,
          {
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            },
          }
        );

        if (response.status === 200) {
          await this.handleProfilePictureChange();
          alert("수정에 성공했습니다.");
          this.$router.push("/member/profile");
        }
      } catch (error) {
        console.error("Error modifying profile", error);
      }
    },

    // 파일 업로드 핸들러
    handleFileUpload(event) {
      const selectedFile = event.target.files[0];
      if (selectedFile) {
        this.previewUrl = URL.createObjectURL(selectedFile); // 미리보기 URL 생성
        this.profileImage = selectedFile;
      }
    },

    // 프로필 사진 변경 메서드
    async handleProfilePictureChange() {
      if (this.shouldDeleteOldProfilePicture()) {
        await this.deleteOldProfilePicture();
      }

      if (this.profileImage) {
        await this.uploadNewProfilePicture();
      }
    },

    // 기존 프로필 사진 삭제 여부 확인
    shouldDeleteOldProfilePicture() {
      return (
        this.profilePicture &&
        this.profilePicture !== this.previewUrl &&
        this.profilePicture !== "/src/components/icons/Profile_default.png"
      );
    },

    // 기존 프로필 사진 삭제 요청
    async deleteOldProfilePicture() {
      try {
        const deleteUrl = "http://localhost/file/delete/member";
        const payload = { fileUrls: [this.profilePicture] };
        await axios.delete(deleteUrl, { data: payload });
      } catch (error) {
        console.error("기존 프로필 사진 삭제 중 오류 발생:", error);
      }
    },

    // 새로운 프로필 사진 업로드
    async uploadNewProfilePicture() {
      try {
        const formData = new FormData();
        formData.append("file", this.profileImage);
        formData.append("associatedTable", "member");
        formData.append("associatedId", this.memberId);

        const response = await axios.post(
          "http://localhost/file/upload",
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
            },
          }
        );

        if (response.status === 200) {
          this.profilePicture = `http://localhost/${response.data[0].saveFolder}/${response.data[0].saveFile}`;

          const authStore = useAuthStore();
          authStore.setProfilePicture(response.data[0].fileUrl);
        }
      } catch (error) {
        console.error("새로운 프로필 사진 업로드 중 오류 발생:", error);
      }
    },
  },
};
</script>
