<template>
  <section>
    <h2
      class="text-[#181013] text-[22px] font-bold leading-tight tracking-[-0.015em] px-4 pb-3 pt-5"
    >
      매칭
    </h2>
    <div class="slider-container">
      <!-- 왼쪽 화살표 버튼 -->
      <button class="slider-button left" @click="scrollLeft">‹</button>
      <!-- 카드 슬라이더 -->
      <div class="slider-wrapper">
        <div
          class="slider"
          :style="{ transform: `translateX(${scrollPosition}px)` }"
        >
          <MainPageMatchCard
            v-for="(item, index) in Matchings"
            :key="index"
            :plan="item.plan"
            :planTitle="item.planTitle"
          />
        </div>
      </div>
      <!-- 오른쪽 화살표 버튼 -->
      <button class="slider-button right" @click="scrollRight">›</button>
    </div>
  </section>
</template>

<script>
import MainPageMatchCard from "./MainPageMatchCard.vue";

export default {
  components: {
    MainPageMatchCard,
  },
  props: {
    Matchings: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      scrollPosition: 0, // 슬라이더의 위치
    };
  },
  methods: {
    scrollLeft() {
      const cardWidth = 225; // 카드 너비 + 간격
      this.scrollPosition = Math.min(this.scrollPosition + cardWidth, 0);
    },
    scrollRight() {
      const cardWidth = 216; // 카드 너비 + 간격
      const maxScroll = -(this.matches.length * cardWidth - cardWidth * 3); // 최대 스크롤 제한
      this.scrollPosition = Math.max(
        this.scrollPosition - cardWidth,
        maxScroll
      );
    },
  },
};
</script>

<style scoped>
/* 슬라이더 컨테이너 */
.slider-container {
  position: relative;
  width: 100%;
  overflow: hidden;
  padding: 20px 0;
}

/* 슬라이더 래퍼 */
.slider-wrapper {
  overflow: hidden;
  width: calc(100% - 100px); /* 화살표 버튼 공간 확보 */
  margin: 0 auto;
  position: relative;
}

/* 슬라이더 */
.slider {
  display: flex;
  transition: transform 0.5s ease; /* 슬라이드 애니메이션 */
}

/* 카드 간격 */
.slider > * {
  flex: 0 0 auto;
  width: 200px; /* 카드 크기 고정 */
  margin-right: 16px;
}

/* 화살표 버튼 */
.slider-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: #fff;
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1;
}

.slider-button.left {
  left: 10px;
}

.slider-button.right {
  right: 10px;
}

.slider-button:hover {
  background-color: rgba(0, 0, 0, 0.8);
}
</style>
