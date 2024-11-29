  <template>
    <div class="container">
      <div class="search-container">
        <!-- 첫 번째 드롭다운: 시도 -->
        <label class="select-label">
          <select v-model="selectedSido" class="select-input" @change="fetchGuguns">
            <option value="" :selected="!selectedSido" disabled>시도</option>
            <option
              v-for="sido in sidos"
              :key="sido.sidoCode"
              :value="sido.sidoCode"
            >
              {{ sido.sidoName }}
            </option>
          </select>
        </label>

        <!-- 두 번째 드롭다운: 구군 -->
        <label class="select-label">
          <select v-model="selectedGugun" :disabled="isGugunDisabled" class="select-input">
            <option value="" disabled>구군</option>
            <option
              v-for="gugun in guguns"
              :key="gugun.gugunCode"
              :value="gugun.gugunCode"
            >
              {{ gugun.gugunName }}
            </option>
          </select>
        </label>

        <!-- 세 번째 드롭다운: 관광지 종류 -->
        <label class="select-label">
          <select v-model="selectedType" class="select-input">
            <option value="" disabled>관광지 종류</option>
            <option
              v-for="type in contentTypes"
              :key="type.contentTypeId"
              :value="type.contentTypeId"
            >
              {{ type.contentTypeName }}
            </option>
          </select>
        </label>

        <!-- 텍스트 입력 필드: 검색어 입력 -->
        <label class="text-input-label">
          <input
            type="text"
            v-model="searchQuery"
            placeholder="검색어를 입력하세요"
            class="text-input"
          />
        </label>

        <!-- 검색 버튼 -->
        <button @click="handleSearch" class="search-button">검색</button>
      </div>

      <!-- 검색 결과 표시 영역 -->
      <div class="results-container" v-if="searchResults.length">
        <!-- 캐러셀 컨테이너 -->
        <div class="carousel-container">
          <button class="carousel-button left" @click="scrollLeft">&lt;</button>
          <draggable
            :list="searchResults"
            :group="{ name: 'items', pull: 'clone', put: false }"
            :sort="false"
            :clone="cloneResult"
            item-key="contentId"
            class="carousel-track"
            ref="carouselTrack"
          >
            <template #item="{ element }">
              <ResultCard :result="element" @click="selectResult(element)" />
            </template>
          </draggable>
          <button class="carousel-button right" @click="scrollRight">&gt;</button>
        </div>
      </div>
      <div v-else-if="searchPerformed">
        <p>검색 결과가 없습니다.</p>
      </div>

      <div class="results-container" v-if="rentalCarResults.length">
        <h3>렌터카 검색 결과</h3>
        <!-- Carousel Container -->
        <div class="carousel-container">
          <button class="carousel-button left" @click="scrollLeft('rental')">&lt;</button>
          <draggable
            :list="rentalCarResults"
            :group="{ name: 'items', pull: 'clone', put: false }"
            :sort="false"
            item-key="id"
            class="carousel-track"
            ref="rentalCarouselTrack"
          >
            <template #item="{ element }">
              <RentalCarResultCard :result="element" @click="selectRentalResult(element)" />
            </template>
          </draggable>
          <button class="carousel-button right" @click="scrollRight('rental')">&gt;</button>
        </div>
      </div>

      <!-- 선택된 결과 상세 정보 표시 영역 -->
      <div class="details-container">
        <h2 v-if="selectedResult">{{ selectedResult.title }}</h2>
        <div id="map" style="width: 100%; height: 400px;"></div>
        <KakaoMap :lat="this.defaultLatLng.latitude" :lng="this.defaultLatLng.longitude"></KakaoMap>
      </div>


    </div>
  </template>

  <script>
  /* global kakao */
  import { KakaoMap } from "vue3-kakao-maps";
  import axios from "axios";
  import ResultCard from "./ResultCard.vue";
  import draggable from "vuedraggable";
  import RentalCarResultCard from "./RentalCarResultCard.vue";

  export default {
    name: "TourPlanSidebar",
    components: {
      ResultCard,
      draggable,
      RentalCarResultCard,
    },
    props: {
      selectedResult: {
        type: Object,
        default: null,
      },
      rentalCarResults: { // Add this prop
        type: Array,
        default: () => [],
      },
    },
    computed: {
      isGugunDisabled() {
        return !this.guguns.length;
      },
    },

    data() {
      return {
        sidos: [],
        guguns: [],
        contentTypes: [],
        selectedSido: "",
        selectedGugun: "",
        selectedType: "",
        searchQuery: "",
        searchResults: [],
        searchPerformed: false,
        kakaoMapLoaded: false,
        map: null, // Kakao 지도 객체
        marker: null, // 현재 표시된 마커
        defaultLatLng: { latitude: 37.5665, longitude: 126.9780 },
      };
    },
    watch: {
      selectedResult(newResult) {
        if (newResult && newResult.latitude && newResult.longitude) {
          if (this.kakaoMapLoaded) {
            this.updateMap(newResult);
          } else {
            this.loadKakaoMapScript().then(() => {
              this.initMap(newResult);
            });
          }
        } else {
          console.error("선택된 결과에 위치 정보가 없습니다.");
        }
      },
    },

    mounted() {
      this.loadKakaoMapScript().then(() => {
        this.initMap(this.defaultLatLng);
      });
      this.fetchSidos();
      this.fetchContentTypes();
    },

    methods: {
      async fetchSidos() {
        try {
          const response = await axios.get("http://localhost/trip/getSidos");
          if (response.status === 200) {
            this.sidos = response.data;
          }
        } catch (error) {
          console.error("시도 데이터를 가져오지 못했습니다:", error);
        }
      },
      async fetchGuguns() {
        if (!this.selectedSido) return;
        try {
          const response = await axios.get("http://localhost/trip/getGuguns", {
            params: { sidoCode: this.selectedSido },
          });
          if (response.status === 200) {
            this.guguns = response.data;
          }
        } catch (error) {
          console.error("구군 데이터를 가져오지 못했습니다:", error);
        }
      },
      async fetchContentTypes() {
        try {
          const response = await axios.get("http://localhost/trip/getType");
          if (response.status === 200) {
            this.contentTypes = response.data;
          }
        } catch (error) {
          console.error("관광지 종류 데이터를 가져오지 못했습니다:", error);
        }
      },
      async handleSearch() {
        if (!this.selectedSido && !this.searchQuery) {
          alert("시도 또는 검색어를 입력하세요.");
          return;
        }

        try {
          const response = await axios.get("http://localhost/trip/search", {
            params: {
              sidoCode: this.selectedSido || null,
              gugunCode: this.selectedGugun || null,
              contentTypeId: this.selectedType || null,
              keyword: this.searchQuery || "",
            },
            headers: { "Content-Type": "application/json" },
          });

          this.searchResults = response.data || [];
          this.searchPerformed = true;
          //this.selectedResult = null;
        } catch (error) {
          console.error("검색 중 에러 발생:", error);
        }
      },
      selectResult(result) {
        //this.selectedResult = result;
        this.$emit("update:selectedResult", result);

        if (this.selectedResult?.latitude && this.selectedResult?.longitude) {
          if (this.kakaoMapLoaded) {
            this.updateMap(result);
          } else {
            this.loadKakaoMapScript().then(() => {
              this.initMap(result);
            });
          }
        } else {
          console.error("선택된 결과에 위치 정보가 없습니다.");
        }
      },
      selectRentalResult(result) {
      this.$emit("update:selectedResult", result);

      if (result.latitude && result.longitude) {
        if (this.kakaoMapLoaded) {
          this.updateMap(result);
        } else {
          this.loadKakaoMapScript().then(() => {
            this.initMap(result);
          });
        }
      } else {
        console.error("선택된 결과에 위치 정보가 없습니다.");
      }
    },
      scrollLeft() {
        const track = this.$refs.carouselTrack.$el;
        track.scrollLeft -= 200;
      },
      scrollRight() {
        const track = this.$refs.carouselTrack.$el;
        track.scrollLeft += 200;
      },
      loadKakaoMapScript() {
        return new Promise((resolve, reject) => {
          if (typeof kakao !== "undefined" && kakao.maps) {
            kakao.maps.load(() => {
              this.kakaoMapLoaded = true;
              resolve();
            });
          } else {
            window.kakaoMapCallback = () => {
              kakao.maps.load(() => {
                this.kakaoMapLoaded = true;
                resolve();
              });
            };
            const script = document.createElement("script");
            script.src =
              "https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=8bfa1062da3bfab1a2423750e935e3d3";
            script.onerror = reject;
            document.head.appendChild(script);
          }
        });
      },
      initMap(result) {
        kakao.maps.load(() => {
          const container = document.getElementById("map");
          if (!container) {
            console.error("지도 컨테이너를 찾을 수 없습니다.");
            return;
          }

          const options = {
            center: new kakao.maps.LatLng(this.defaultLatLng.latitude, this.defaultLatLng.longitude),
            level: 3,
          };
          this.map = new kakao.maps.Map(container, options);


          const position = new kakao.maps.LatLng(result.latitude, result.longitude);
          this.marker = new kakao.maps.Marker({ position });
          this.marker.setMap(this.map);
        });
      },
      updateMap(result) {
        if (!this.map) {
          this.initMap(result);
          return;
        }

        const position = new kakao.maps.LatLng(result.latitude, result.longitude);
        this.map.setCenter(position);

        // 기존 마커를 제거하고 새로운 마커를 추가합니다.
        if (this.marker) {

          this.marker.setMap(null);
        }
        this.marker = new kakao.maps.Marker({ position });
        this.marker.setMap(this.map);
      },
    },
    addMarker(latitude, longitude) {
        // 기존 마커 제거
        this.clearMarkers();

        const position = new kakao.maps.LatLng(latitude, longitude);
        const marker = new kakao.maps.Marker({
          position,
        });

        marker.setMap(this.map);
        this.markers.push(marker);
      },

  };
  </script>


  <style scoped>
  .container {
    padding: 3%;
  }

  /* 검색 영역 스타일 */
  .search-container {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    margin-bottom: 20px;
    align-items: flex-end;
  }

  .select-label,
  .text-input-label {
    display: flex;
    flex-direction: column;
  }

  .select-input,
  .text-input {
    padding: 8px 12px;
    border: 1px solid #ccc;
    border-radius: 4px;
    width: 100px;
  }

  .text-input {
    width: 300px;
  }

  .search-button {
    padding: 10px 20px;
    background-color: #fc6986;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  .search-button:hover {
    background-color: #e55b7e;
  }

  .results-container {
    background-color: #fff;
    padding: 15px;
    border-radius: 8px;
  }

  .carousel-container {
    display: flex;
    align-items: center;
  }

  .carousel-button {
    font-size: 2rem;
    cursor: pointer;
  }

  .carousel-track {
    display: flex;
    overflow-x: auto;
    scroll-behavior: smooth;
  }

  .details-container {
    margin-top: 20px;
  }

  .details-container h2 {
    font-size: 1.5rem;
  }
  </style>
