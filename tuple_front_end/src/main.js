import { createPinia } from "pinia";
import { createApp } from "vue";
import { useKakao } from "vue3-kakao-maps/@utils";

// Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css";
// Bootstrap JavaScript (필요한 경우에만 사용)
import "bootstrap/dist/js/bootstrap.bundle.min.js";

import "./assets/tailwind.css";

import App from "./App.vue";
import router from "./router";

const app = createApp(App);

app.use(createPinia());
app.use(router);
useKakao("b518e823e14f5f456d68c9419e9f83a7");

import { useAuthStore } from "@/stores/auth";
const authStore = useAuthStore();
authStore.initialize(); // 애플리케이션 로드 시 프로필 초기화

app.mount("#app");
