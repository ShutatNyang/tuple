/** @type {import('tailwindcss').Config} */
export default {
  mode: "jit", // JIT 모드 활성화
  content: [
    "./index.html",
    "./src/**/*.{vue,js,jsx}", // src 폴더 내의 모든 Vue, JavaScript, TypeScript 파일
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require("tailwind-scrollbar"), // Scrollbar 플러그인 추가
  ],
  // 필요 시 variants 추가
  variants: {
    scrollbar: ["rounded"], // 원하는 변형 추가
  },
};
