<template>
  <div
    class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden"
    :style="{ fontFamily: 'Plus Jakarta Sans, Noto Sans, sans-serif' }"
  >
    <div class="layout-container flex h-full grow flex-col">
      <div class="flex flex-1 justify-center items-center py-5" id="pink">
        <div
          class="layout-content-container flex flex-col w-full max-w-[960px] flex-1"
          id="white"
        >
          <!-- Board Tabs Section -->
          <div
            class="flex gap-3 p-3 flex-wrap pr-4"
            v-if="$route.meta.showHeaderFooter"
          >
            <BoardTab />
          </div>

          <RouterView v-slot="{ Component, route }">
            <component
              :is="Component"
              :update-page="currentPage"
              v-bind="route.props"
            />
          </RouterView>

          <!-- Board Items -->
          <div v-if="$route.meta.showHeaderFooter">
            <BoardFooter @update-page="handlePageUpdate" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BoardTab from "../board/BoardTab.vue";
import BoardFooter from "../board/BoardFooter.vue";
import { RouterView } from "vue-router";
import { useRoute } from "vue-router";

export default {
  components: {
    BoardTab,
    RouterView,
    BoardFooter,
  },
  props: {
    events: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      currentPage: 1,
    };
  },
  methods: {
    handlePageUpdate(newPage) {
      this.currentPage = newPage;
    },
  },
};
</script>

<style scoped>
#pink {
  background-color: #f0dbe2; /* 핑크색 배경 */
  display: flex;
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
}

#white {
  background-color: #fff; /* 하얀색 박스 */
  border-radius: 3%; /* 둥근 모서리 */
  padding: 20px; /* 내부 여백 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 박스 그림자 */
  max-width: 960px; /* 최대 너비 */
  width: 100%; /* 반응형 너비 */
}
</style>
