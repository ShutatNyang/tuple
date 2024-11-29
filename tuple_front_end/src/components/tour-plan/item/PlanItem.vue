<template>
  <div
    class="flex flex-col gap-4 p-4 border rounded-xl border-[#e6dbdd]"
  >
    <!-- Day Label -->
    <h4 class="text-[#181112] text-base font-medium leading-normal pb-2">
      Day {{ day }}
    </h4>

    <!-- 드래그 가능한 리스트 추가 -->
    <draggable
      v-model="localItem.results"
      :group="{ name: 'items', pull: true, put: true }"
      :sort="true"
      item-key="contentId"
      class="result-cards"
    >
      <template #item="{ element, index }">
        <div class="result-card-item">
          <!-- Result Card 컴포넌트 -->
          <ResultCard
            :result="element"
            @click="selectResult(element)"
          />
          <button @click.stop="removeScheduleItem(index)" class="remove-button">
            Remove
          </button>
        </div>
      </template>
    </draggable>

    <!-- 경로 추천 버튼 -->
    <div v-if="localItem.results.length > 1" class="recommend-route">
      <button @click.stop="recommendRoute" class="recommend-button">
        경로 추천
      </button>
    </div>

    <!-- Short Description Input -->
    <label class="flex flex-col">
      <p class="text-[#181112] text-base font-medium leading-normal pb-2">
        짧은 설명
      </p>
      <input
        v-model="localItem.shortDescription"
        type="text"
        class="form-input flex w-full min-w-0 flex-1 rounded-xl text-[#181112] focus:outline-0 focus:ring-0 border border-[#e6dbdd] bg-white focus:border-[#e6dbdd] h-10 placeholder:text-[#8c5f68] p-[10px] text-base font-normal leading-normal"
        placeholder="간단한 설명을 입력해주세요"
      />
    </label>
  </div>
</template>

<script>
/* global kakao */
import axios from "axios";
import draggable from "vuedraggable";
import ResultCard from "./ResultCard.vue";

export default {
  name: "PlanItem",
  components: {
    draggable,
    ResultCard,
  },
  props: {
    item: {
      type: Object,
      required: true,
    },
    day: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      localItem: JSON.parse(JSON.stringify(this.item)), // Props 복사본으로 로컬 데이터 생성
    };
  },
  methods: {
    /**
     * 드래그 리스트에서 특정 아이템 제거
     */
    removeScheduleItem(index) {
      this.localItem.results.splice(index, 1); // 로컬 복사본 수정
      this.$emit("update:item", this.localItem); // 부모에게 변경사항 전달
    },

    /**
     * ResultCard 클릭 시 부모로 전달
     */
    selectResult(result) {
      this.$emit("show-results", result);
    },

    /**
     * PlanItem 데이터를 부모로 반환
     */
    getItemData() {
      return {
        ...this.localItem, // 현재 localItem 반환
      };
    },

    /**
     * 경로 추천 요청
     */
     async recommendRoute() {
    try {
      const response = await axios.post("http://localhost/trip/getPath", {
        startId: this.localItem.results[0]?.contentId ?? null,
        endId: this.localItem.results[this.localItem.results.length - 1]?.contentId ?? null,
        waypointIds: this.localItem.results
          .slice(1, this.localItem.results.length - 1)
          .map((item) => item.contentId),
      });

      if (response.status === 200) {
        const optimalPath = response.data; // 최적화된 경로를 반환받음

        // Polyline을 그릴 경로 점(위도, 경도) 설정
        const coordinates = optimalPath.map((point) => ({
          lat: point.latitude,
          lng: point.longitude,
        }));

        this.drawRouteOnMap(coordinates);

        // 기존 Proxy 배열에서 순서 변경
        const noOrder = optimalPath.map((item) => item.no);
        this.localItem.results.sort(
          (a, b) => noOrder.indexOf(a.no) - noOrder.indexOf(b.no)
        );

        this.$emit("update:item", this.localItem); // 부모 컴포넌트에 변경 전파
      }
    } catch (error) {
      console.error("경로 추천 요청 실패:", error);
    }
  },

  /**
   * 카카오맵에 경로 그리기
   */
  drawRouteOnMap(coordinates) {
    // 지도 초기화
    if (!this.map) {
      this.map = new kakao.maps.Map(document.getElementById("map"), {
        center: new kakao.maps.LatLng(coordinates[0].lat, coordinates[0].lng),
        level: 4, // 지도 확대 수준
      });
    }

    // 기존 마커와 폴리라인 제거
    if (this.marker) {
      this.marker.setMap(null);
    }
    if (this.polyline) {
      this.polyline.setMap(null);
    }

    // 경로를 따라 마커 추가
    coordinates.forEach((coord, index) => {
      new kakao.maps.Marker({
        map: this.map,
        position: new kakao.maps.LatLng(coord.lat, coord.lng),
        title: `경유지 ${index + 1}`,
      });
    });

    // Polyline 객체 생성
    const linePath = coordinates.map(
      (coord) => new kakao.maps.LatLng(coord.lat, coord.lng)
    );

    this.polyline = new kakao.maps.Polyline({
      path: linePath, // 경로 좌표 배열
      strokeWeight: 5, // 선 두께
      strokeColor: "#FF0000", // 선 색상
      strokeOpacity: 0.8, // 선 불투명도
      strokeStyle: "solid", // 선 스타일
    });

    // 지도에 Polyline 표시
    this.polyline.setMap(this.map);

    // 지도의 중심과 확대 수준을 경로에 맞게 조정
    const bounds = new kakao.maps.LatLngBounds();
    linePath.forEach((point) => bounds.extend(point));
    this.map.setBounds(bounds);
    coordinates = [];
  },

  },
};
</script>

<style scoped>
.result-cards {
  display: flex;
  flex-direction: column;
  gap: 10px;
  min-height: 50px;
  border: 1px dashed #ccc;
  padding: 10px;
}

.result-card-item {
  display: flex;
  align-items: center;
}

.remove-button {
  margin-left: 10px;
  background-color: #fc6986;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
}

.remove-button:hover {
  background-color: #e55b7e;
}

.recommend-route {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.recommend-button {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  font-size: 14px;
  cursor: pointer;
}

.recommend-button:hover {
  background-color: #45a049;
}
</style>
