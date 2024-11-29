<template>
  <div class="flex justify-center mt-4">
    <button
      class="px-3 py-1 border rounded"
      :disabled="currentPage === 1"
      @click="$emit('update:current-page', currentPage - 1)"
    >
      이전
    </button>
    <span class="px-3 py-1">{{ currentPage }} / {{ totalPages }}</span>
    <button
      class="px-3 py-1 border rounded"
      :disabled="currentPage === totalPages"
      @click="$emit('update:current-page', currentPage + 1)"
    >
      다음
    </button>
  </div>
</template>

<script>
export default {
  name: "Pagination",
  props: {
    total: { type: Number, required: true },
    currentPage: { type: Number, required: true },
  },
  computed: {
    totalPages() {
      return Math.ceil(this.total / 2); // itemsPerPage is 2
    },
  },
};
</script>
