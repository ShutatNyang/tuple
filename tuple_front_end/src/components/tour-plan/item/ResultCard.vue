<template>
  <div class="card" @click="handleClick">
    <img
      :src="result.firstImage1 || defaultImage"
      alt="썸네일 이미지"
      class="card-image"
    />
    <p class="card-name">{{ result.title }}</p>
  </div>
</template>

<script>
export default {
  name: "ResultCard",
  props: {
    result: {
      type: Object,
      required: true,
    },
    isFirst: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    handleClick() {
      this.$emit("select", this.result);
    },
  },
};
</script>

<style scoped>
.card {
  flex: 0 0 auto;
  width: 150px;
  margin-right: 10px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.card:hover {
  transform: scale(1.05);
}

.card-image {
  width: 100%;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
}

.card-name {
  margin-top: 5px;
  font-size: 1rem;
  font-weight: bold;
  color: #333;
}
</style>
