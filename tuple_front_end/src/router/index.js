import BoardDetailView from "@/components/board/BoardDetailView.vue";
import BoardModify from "@/components/board/BoardModify.vue";
import BoardWrite from "@/components/board/BoardWrite.vue";
import ImageBoard from "@/components/board/ImageBoard.vue";
import TextBoard from "@/components/board/TextBoard.vue";
import Chat from "@/components/chat/Chat.vue";
import ChatRoom from "@/components/chat/ChatRoom.vue";
import ForgotPassword from "@/components/email/ForgotPassword.vue";
import ResetPassword from "@/components/email/ResetPassword.vue";
import Verify from "@/components/email/Verify.vue";
import MatchingList from "@/components/match/MatchingList.vue";
import MyMatching from "@/components/match/MyMatching.vue";
import ForgotEmail from "@/components/member/ForgotEmail.vue";
import MemberLogin from "@/components/member/MemberLogin.vue";
import MemberMyProfile from "@/components/member/MemberMyProfile.vue";
import MemberProfileModify from "@/components/member/MemberProfileModify.vue";
import MemberSignUp from "@/components/member/MemberSignUp.vue";
import PlanList from "@/components/tour-plan/PlanList.vue";
import TourPlanDetail from "@/components/tour-plan/TourPlanDetail.vue";
import TourPlanWrite from "@/components/tour-plan/TourPlanWrite.vue";
import BoardView from "@/components/views/BoardView.vue";
import MainView from "@/components/views/MainView.vue";
import MatchingView from "@/components/views/MatchingView.vue";
import MemberView from "@/components/views/MemberView.vue";
import TourPlanView from "@/components/views/TourPlanView.vue";
import MatchingDetail from "@/components/match/MatchingDetail.vue";
import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "MainView",
    component: MainView, // 기본 메인 페이지
  },
  {
    path: "/board",
    component: BoardView, // 게시판 페이지
    meta: { showHeaderFooter: true },
    children: [
      {
        path: "",
        redirect: "/board/notice", // 기본 경로로 리디렉션 설정
        meta: { showHeaderFooter: true },
      },
      {
        path: "notice",
        name: "NoticeBoardItem",
        component: TextBoard,
        props: { boardType: "notice" }, // boardType 전달
        meta: { showHeaderFooter: true },
      },
      {
        path: "free",
        name: "FreeBoardItem",
        component: TextBoard,
        props: { boardType: "free" }, // boardType 전달
        meta: { showHeaderFooter: true },
      },
      {
        path: "event",
        name: "EventBoardItem",
        component: ImageBoard,
        props: { boardType: "event" }, // boardType 전달
        meta: { showHeaderFooter: true },
      },
      {
        path: "review",
        name: "TourReviewBoardItem",
        component: ImageBoard,
        props: { boardType: "review" }, // boardType 전달
        meta: { showHeaderFooter: true },
      },
      {
        path: "qna",
        name: "QnABoardItem",
        component: TextBoard,
        props: { boardType: "qna" }, // boardType 전달
        meta: { showHeaderFooter: true },
      },
      {
        path: ":boardName/write",
        name: "BoardWrite",
        component: BoardWrite,
        meta: { showHeaderFooter: false },
        props: true,
      },
      {
        path: ":boardName/detail/:id",
        name: "BoardDetailView",
        component: BoardDetailView,
        meta: { showHeaderFooter: false },
        props: true,
      },
      {
        path: ":boardName/modify/:id",
        name: "BoardModify",
        component: BoardModify,
        meta: { showHeaderFooter: false },
        props: true,
      },
    ],
  },
  {
    path: "/member",
    component: MemberView,
    children: [
      {
        path: "",
        redirect: "/member/login", // 기본 경로로 리디렉션 설정
      },
      {
        path: "login",
        name: "MemberLogin",
        component: MemberLogin, // 로그인 페이지
      },
      {
        path: "sign-up",
        name: "MemberSignUp",
        component: MemberSignUp, // 회원가입 페이지
      },
      {
        path: "profile",
        name: "MemberMyProfile",
        component: MemberMyProfile, // 마이 프로필 페이지
      },
      {
        path: "modify",
        name: "MemberModify",
        component: MemberProfileModify, // 프로필 수정 페이지
      },
    ],
  },
  {
    path: "/email-verify",
    name: "Verify",
    component: Verify,
  },
  {
    path: "/reset-password",
    name: "ResetPassword",
    component: ResetPassword,
  },
  {
    path: "/forgot-password",
    name: "ForgotPassword",
    component: ForgotPassword,
  },
  {
    path: "/forgot-email",
    name: "ForgotEmail",
    component: ForgotEmail,
  },
  {
    path: "/tour-plan",
    component: TourPlanView, // 공통 레이아웃 컴포넌트
    children: [
      {
        path: "",
        redirect: "/tour-plan/my-plan",
      },
      {
        path: "my-plan",
        name: "MyPlan",
        component: PlanList,
        props: { showAll: false },
      },
      {
        path: "planListAll",
        name: "PlanListAll",
        component: PlanList,
        props: { showAll: true },
      },
      {
        path: "detail/:id",
        name: "TourPlanDetail",
        component: TourPlanDetail,
        props: true,
      },
    ],
  },
  {
    path: "/tour-plan/write-plan",
    name: "TourPlanWrite",
    component: TourPlanWrite,
  },
  {
    path: "/tour-plan/modify/:id",
    name: "TourPlanModify",
    component: () => import("@/components/tour-plan/TourPlanWrite.vue"),
    props: true,
  },
  {
    path: "/chat-room",
    component: ChatRoom,
  },
  {
    path: "/chat/:id",
    component: Chat,
  },
  {
    path: "/match",
    component: MatchingView,
    children: [
      {
        path: "",
        redirect: "/match/my",
      },
      {
        path: "my",
        name: "MyMatching",
        component: MyMatching,
        props: { showAll: false },
      },
      {
        path: "list",
        name: "MatchingList",
        component: MatchingList,
        props: { showAll: true },
      },
      {
        path: "detail/:id",
        name: "MatchingDetail",
        component: MatchingDetail,
        props: true,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
