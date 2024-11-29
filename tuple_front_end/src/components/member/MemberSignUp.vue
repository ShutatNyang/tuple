<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden"
    :style="{
      'font-family': 'Plus Jakarta Sans, Noto Sans, sans-serif',
    }"
  >
    <MemberHeader />
    <div class="layout-container flex h-full grow flex-col">
      <div class="px-40 flex flex-1 justify-center items-center">
        <div
          class="layout-content-container flex flex-col justify-center items-center w-[700px] max-w-[700px] py-5 max-w-[960px]"
        >
          <h3
            class="text-[#111418] tracking-light text-2xl font-bold leading-tight px-4 text-center pb-2 pt-3"
          >
            Create an account
          </h3>
          <form @submit.prevent="signUp">
            <!-- 가운데 정렬 -->
            <div class="flex flex-col items-center w-full space-y-4 w-[800px]">
              <!-- 이메일 입력 -->
              <div
                class="flex flex-col items-center max-w-[550px] gap-4 px-4 py-3 w-[550px]"
              >
                <label class="flex flex-col min-w-40 w-full">
                  <p
                    class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
                  >
                    ID (email)
                  </p>
                  <div class="flex w-full gap-2">
                    <input
                      class="form-input flex w-3/4 resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border-none bg-[#f4e6e9] h-14 placeholder:text-[#9e4759] p-4 text-base font-normal"
                      v-model="email"
                      placeholder="이메일 인증과 로그인 아이디로 사용됩니다."
                    />
                    <button
                      type="button"
                      class="w-1/4 bg-[#9e4759] hover:bg-[#8d2c3d] text-white font-medium rounded-xl h-14 text-base"
                      @click="checkDuplicate"
                    >
                      중복확인
                    </button>
                  </div>
                  <p
                    v-if="emailError"
                    :class="
                      emailError.includes('중복된')
                        ? 'text-red-500'
                        : 'text-blue-500'
                    "
                    class="text-sm mt-2"
                  >
                    {{ emailError }}
                  </p>
                </label>
              </div>

              <!-- 비밀번호 입력 -->
              <div
                class="flex flex-col items-center max-w-[550px] gap-4 px-4 py-3 w-full"
              >
                <label class="flex flex-col min-w-40 w-full">
                  <p
                    class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
                  >
                    Password
                  </p>
                  <input
                    class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border-none bg-[#f4e6e9] h-14 placeholder:text-[#9e4759] p-4 text-base font-normal"
                    v-model="password"
                    placeholder="password"
                    type="password"
                  />
                </label>
              </div>

              <div
                class="flex flex-col items-center max-w-[550px] gap-4 px-4 py-3 w-full"
              >
                <label class="flex flex-col min-w-40 w-full">
                  <p
                    class="text-[#1c0d10] text-base font-medium leading-normal pb-2"
                  >
                    Password Check
                  </p>
                  <input
                    class="form-input flex w-full resize-none overflow-hidden rounded-xl text-[#1c0d10] focus:outline-0 focus:ring-0 border-none bg-[#f4e6e9] h-14 placeholder:text-[#9e4759] p-4 text-base font-normal"
                    v-model="passwordCheck"
                    placeholder="password check"
                    type="password"
                  />
                  <p
                    v-if="passwordError"
                    :class="
                      passwordError.includes('일치하지 않습니다')
                        ? 'text-red-500'
                        : 'text-blue-500'
                    "
                    class="text-sm mt-2"
                  >
                    {{ passwordError }}
                  </p>
                </label>
              </div>

              <!-- 이름 입력 -->
              <div
                class="flex flex-col items-center max-w-[550px] gap-4 px-4 py-3 w-full"
              >
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
                  Create Account
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
import axios from "axios";
import MemberHeader from "./MemberHeader.vue";

export default {
  components: {
    MemberHeader,
  },
  data() {
    return {
      email: "",
      emailError: "",
      emailDupl: 0,
      password: "",
      passwordCheck: "",
      passwordError: "",
      name: "",
      nickname: "",
      phone: "",
      age: "",
      gender: "",
      genderOptions: ["Male", "Female", "Other"],
      description: "",

      ProfileDefault,
      file: null,
      previewUrl: "", // 업로드한 파일 미리보기 URL
      profileImage: null,
    };
  },
  methods: {
    // 데이터를 URL-encoded 형식으로 변환
    toUrlEncoded(data) {
      return Object.keys(data)
        .map(
          (key) => `${encodeURIComponent(key)}=${encodeURIComponent(data[key])}`
        )
        .join("&");
    },
    async signUp() {
      if (this.emailDupl !== 2) {
        alert("이메일 입력을 확인해주세요");
        return;
      }
      if (this.password === "") {
        alert("비밀번호는 필수 입니다.");
        return;
      }

      const memberData = {
        email: this.email.trim(),
        password: this.password.trim(),
        name: this.name.trim(),
        phone: this.phone.trim(),
        nickname: this.nickname.trim(),
        age: this.age,
        gender: this.gender,
        description: this.description,
      };

      try {
        const response = await axios.post(
          "http://localhost/member/regist",
          this.toUrlEncoded(memberData), // 데이터 URL-encoded 형식으로 변환
          {
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
          }
        );

        await axios.post(
          "http://localhost/email/send-verification?email=" + memberData.email
        );

        if (response.status === 200) {
          if (this.profileImage) {
            await this.uploadProfileImage(response.data);
          }

          alert(
            "회원가입이 성공적으로 완료되어 가입하신 이메일로 인증 이메일이 발송되었습니다."
          );
          this.$router.push("/member/login");
        }
      } catch (error) {
        console.error("회원가입 요청 실패:", error.response || error);
        alert("회원가입에 실패했습니다. 다시 시도해주세요.");
      }
    },
    async checkDuplicate() {
      if (!this.email.trim()) {
        this.emailDupl = 0;
        this.emailError = "이메일을 입력해주세요.";
        return;
      }

      try {
        const response = await axios.get(
          `http://localhost/member/idCheck/` + this.email.trim()
        );
        if (response.data.bool) {
          this.emailError = "중복된 아이디 입니다.";
          this.emailDupl = 1;
        } else {
          this.emailError = "사용 가능한 아이디 입니다.";
          this.emailDupl = 2;
        }
      } catch (error) {
        console.error("중복확인 요청 실패:", error.response || error);
        this.emailError = "중복확인 중 문제가 발생했습니다. 다시 시도해주세요.";
      }
    },
    validatePasswords() {
      if (this.password === this.passwordCheck && this.password !== "") {
        this.passwordError = "비밀번호가 일치합니다.";
      } else if (this.passwordCheck !== "") {
        this.passwordError = "비밀번호가 일치하지 않습니다.";
      } else {
        this.passwordError = "";
      }
    },
    handleFileUpload(event) {
      const selectedFile = event.target.files[0];
      if (selectedFile) {
        this.file = selectedFile;
        this.previewUrl = URL.createObjectURL(selectedFile); // 미리보기 URL 생성
        this.profileImage = selectedFile;
      }
    },
    async uploadProfileImage(memberId) {
      try {
        // FormData 생성
        const formData = new FormData();
        formData.append("file", this.profileImage); // 선택된 파일
        formData.append("associatedTable", "member"); // 관련 테이블 이름
        formData.append("associatedId", memberId); // memberId를 associatedId로 사용

        // 파일 업로드 API 호출
        const uploadResponse = await axios.post(
          "http://localhost/file/upload/signup",
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
      } catch (error) {
        console.error("파일 업로드 실패:", error.response || error);
      }
    },
  },
  watch: {
    password: "validatePasswords",
    passwordCheck: "validatePasswords",
  },
};
</script>
