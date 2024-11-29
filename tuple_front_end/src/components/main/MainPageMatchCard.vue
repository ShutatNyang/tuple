<template>
  <div class="main-page-match-card">
    <div
      class="card-image"
      :style="{ backgroundImage: `url(${firstImage})` }"
    ></div>
    <h3 class="card-title">{{ planTitle }}</h3>
  </div>
</template>

<script>
export default {
  props: {
    plan: {
      type: JSON,
      required: true,
    },
    planTitle: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      firstImage: null, // 첫 번째 이미지 URL 저장
    };
  },
  methods: {
    extractFirstImage() {
      try {
        const thisPlan = JSON.parse(this.plan);

        if (thisPlan && thisPlan.days && thisPlan.days.length > 0) {
          const firstDay = thisPlan.days[0];

          // 첫 번째 날의 locations 배열 확인
          if (firstDay.locations && firstDay.locations.length > 0) {
            // 첫 번째 위치의 이미지 URL 저장
            this.firstImage = firstDay.locations[0].image;
          } else {
            this.firstImage = null; // 기본값 설정
          }
        } else {
          this.firstImage = null; // 기본값 설정
        }
      } catch (error) {
        console.error("JSON 파싱 오류:", error);
        this.firstImage = null; // JSON 파싱 실패 시 기본값 설정
      }
    },
  },
  mounted() {
    this.extractFirstImage(); // 첫 번째 이미지 추출
  },
};
</script>

<style scoped>
.main-page-match-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 150px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  background-color: #fff;
}

.card-image {
  width: 100%;
  aspect-ratio: 16/9;
  background-size: cover;
  background-position: center;
}

.card-title {
  margin-top: 8px;
  font-size: 14px;
  font-weight: bold;
  color: #333;
  text-align: center;
}
</style>
