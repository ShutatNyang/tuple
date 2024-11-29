<template>
  <div class="card" @click="handleClick">
    <p class="card-name">{{ result.name }}</p>
    <p>{{ result.contactInfo }}</p>
  </div>
</template>

<script>
export default {
  name: "RentalCarResultCard",
  props: {
    result: {
      type: Object,
      required: true,
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
