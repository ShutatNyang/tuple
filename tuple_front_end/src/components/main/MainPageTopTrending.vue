<template>
  <section>
    <h2
      class="text-[#181013] text-[22px] font-bold leading-tight tracking-[-0.015em] px-4 pb-3 pt-5"
    >
      인기 관광지
    </h2>
    <div class="grid grid-cols-[repeat(auto-fit,minmax(158px,1fr))] gap-3 p-4">
      <MainPageTopTrendingCard
        v-for="(place, index) in topTrending"
        :key="index"
        :place="place"
      />
    </div>
  </section>
</template>

<script>
import MainPageTopTrendingCard from "./MainPageTopTrendingCard.vue";

export default {
  components: { MainPageTopTrendingCard },
  data() {
    return {
      topTrending: [
        {
          name: "제주도",
          info: "서귀포시",
          image: "/image/jeju.jpg",
        },
        {
          name: "서울",
          info: "강남구",
          image: "/image/gangnam.jpg",
        },
        {
          name: "부산",
          info: "해운대구",
          image: "/image/Haeundae.jpg",
        },
        {
          name: "강원특별자치도",
          info: "강릉시",
          image: "/image/gangreung.jpg",
        },
      ],
    };
  },
};
</script>
