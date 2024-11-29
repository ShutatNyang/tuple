<!-- eslint-disable vue/multi-word-component-names -->
<script setup>
import axios from "axios";
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router'; // useRouter 가져오기

const route = useRoute();
const router = useRouter();
const msg = ref('');

function updateMsg(newMsg) {
  msg.value = newMsg;
}

onMounted(async () => {
  try {
    const response = await axios.get(
      "http://localhost/email/verify?token=" + route.query.token
    );

    updateMsg(response.data);

  } catch (error) {

    updateMsg("오류가 발생했습니다.");
    console.error(error);
  }
});


  function goHome(){
    router.push("/")
  }


</script>

<template>
  <div class="relative flex min-h-screen flex-col bg-white overflow-x-hidden">
    <div class="layout-container flex h-full grow flex-col">
      <div class="flex flex-1 justify-center py-5" id="pink">
        <div
          class="layout-content-container flex flex-col w-full max-w-[960px] flex-1"
          id="white"
        >
          <!-- 메시지 표시 -->
          <div class="px-4 py-5">
            <p class="text-[#181112] text-base font-normal leading-normal">
              {{ msg }}
            </p>
          </div>
          <button @click="goHome">돌아가기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
#pink {
  background-color: #f0dbe2;
  display: flex;
  justify-content: center;
  align-items: center;
}

#white {
  background-color: #fff;
  border-radius: 3%;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 960px;
  width: 100%;
}
</style>

