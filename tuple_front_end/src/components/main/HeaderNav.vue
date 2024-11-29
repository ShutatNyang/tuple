<template>
  <header
    class="flex items-center justify-between whitespace-nowrap border-b border-solid border-b-[#f5f0f1] px-10 py-3"
  >
    <div class="flex items-center gap-4 text-[#181013]">
      <!-- 로고 클릭 시 메인 페이지로 이동 -->
      <img
        src="../icons/Logo.png"
        style="width: 50px; height: 25px; border-radius: 20%; cursor: pointer"
        @click="goHome"
      />
      <h2
        class="text-[#181013] text-lg font-bold leading-tight tracking-[-0.015em]"
      ></h2>
    </div>
    <div class="flex flex-1 justify-end items-center gap-8">
      <nav class="flex items-center gap-9">
        <!-- 게시판 클릭 시 BoardView로 이동 -->
        <router-link
          class="text-[#181013] text-sm font-medium leading-normal"
          to="/board"
        >
          게시판
        </router-link>
        <!-- 매칭 클릭 시 MatchingView로 이동 (경로에 따라 수정) -->
        <router-link
          class="text-[#181013] text-sm font-medium leading-normal"
          to="/match"
        >
          매칭
        </router-link>
        <!-- 여행계획 클릭 시 TourPlanView로 이동 -->
        <router-link
          class="text-[#181013] text-sm font-medium leading-normal"
          to="/tour-plan"
        >
          여행계획
        </router-link>
      </nav>

      <!-- 드롭다운 버튼과 이미지 그룹 -->
      <div class="flex items-center gap-2">
        <div class="relative">
          <button
            ref="dropdownButton"
            class="flex items-center cursor-pointer overflow-hidden rounded-xl h-10 bg-[#f5f0f1] text-[#181013] px-2.5"
            @click="toggleDropdown"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="w-5 h-5"
              fill="currentColor"
              viewBox="0 0 256 256"
            >
              <path
                d="M224,128a8,8,0,0,1-8,8H40a8,8,0,0,1,0-16H216A8,8,0,0,1,224,128ZM40,72H216a8,8,0,0,0,0-16H40a8,8,0,0,0,0,16ZM216,184H40a8,8,0,0,0,0,16H216a8,8,0,0,0,0-16Z"
              />
            </svg>
          </button>

          <div
            v-if="isDropdownVisible"
            class="absolute left-0 mt-3 w-40 bg-white border border-[#f5f0f1] rounded-lg shadow-lg z-10"
          >
            <!-- 드롭다운 메뉴 -->
            <ul class="flex flex-col p-2 text-sm text-[#181013]">
              <li
                v-if="!isUserLoggedIn"
                class="hover:bg-[#f5f0f1] p-2 cursor-pointer"
              >
                <router-link to="/member/login" class="block"
                  >로그인</router-link
                >
              </li>
              <li
                v-if="!isUserLoggedIn"
                class="hover:bg-[#f5f0f1] p-2 cursor-pointer"
              >
                <router-link to="/member/sign-up" class="block"
                  >회원가입</router-link
                >
              </li>
              <li
                v-if="isUserLoggedIn"
                class="hover:bg-[#f5f0f1] p-2 cursor-pointer"
              >
                <router-link to="/member/profile" class="block"
                  >내 프로필</router-link
                >
              </li>
              <li
                v-if="isUserLoggedIn"
                class="hover:bg-[#f5f0f1] p-2 cursor-pointer"
                @click="logout"
              >
                로그아웃
              </li>
            </ul>
          </div>
        </div>

        <img
          v-if="isUserLoggedIn && profilePicture"
          :src="profilePicture"
          alt="User Image"
          class="w-10 h-10 rounded-full"
        />
        <img
          v-else
          src="../icons/Profile_default.png"
          alt="User Image"
          class="w-10 h-10 rounded-full"
        />
      </div>
    </div>
  </header>
</template>

<script>
import { useAuthStore } from "@/stores/auth";
import { computed, onBeforeUnmount, onMounted, ref } from "vue";
import { useRouter } from "vue-router";

export default {
  name: "HeaderNav",
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();
    const isDropdownVisible = ref(false);
    const dropdownButton = ref(null);

    const isUserLoggedIn = computed(() => !!authStore.accessToken);
    const profilePicture = computed(() => authStore.profilePicture);

    const toggleDropdown = () => {
      isDropdownVisible.value = !isDropdownVisible.value;
    };

    const closeDropdown = (event) => {
      if (
        isDropdownVisible.value &&
        dropdownButton.value &&
        !dropdownButton.value.contains(event.target)
      ) {
        isDropdownVisible.value = false;
      }
    };

    const logout = () => {
      authStore.clearLoginUser(); // 로그인 상태 초기화
      router.push("/"); // 로그아웃 후 홈으로 리다이렉트
    };

    const goHome = () => {
      router.push("/");
    };

    onMounted(() => {
      document.addEventListener("click", closeDropdown);
    });

    onBeforeUnmount(() => {
      document.removeEventListener("click", closeDropdown);
    });

    return {
      isDropdownVisible,
      toggleDropdown,
      isUserLoggedIn,
      profilePicture,
      logout,
      dropdownButton,
      goHome,
    };
  },
};
</script>

<style scoped>
/* 필요한 스타일이 있으면 추가하세요 */
</style>
