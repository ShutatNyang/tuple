<template>
  <div class="flex flex-col gap-3 pb-3">
    <div
      class="w-full bg-center bg-no-repeat aspect-square bg-cover rounded-xl"
      :style="{ backgroundImage: `url(${place.image})` }"
    />
    <div>
      <p class="text-[#181013] text-base font-medium leading-normal">
        {{ place.name }}
      </p>
      <p class="text-[#8d5e6d] text-sm font-normal leading-normal">
        {{ place.info }}
      </p>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    place: {
      type: Object,
      required: true,
    },
  },
};
</script>
