<script setup lang="ts">
import { ref, onMounted } from "vue";
import api from "@/api";
import FlickCard from "@/components/organisms/FlickCard.vue";
import FlickCreateForm from "@/components/organisms/FlickCreateForm.vue";
import LoadingStateContainer from "@/components/molecules/LoadingStateContainer.vue";
import type { FlickDetailResponse } from "@/types/api";

const flicks = ref<FlickDetailResponse[]>([]);
const loadingFlicks = ref(true);
const errorFlicks = ref<string | null>(null);

const fetchFlicks = async () => {
  try {
    loadingFlicks.value = true;
    errorFlicks.value = null;
    const response = await api.get<FlickDetailResponse[]>("/flicks/feed");
    flicks.value = response.data;
  } catch (err) {
    errorFlicks.value = "Flickの読み込みに失敗しました。後でもう一度お試しください。";
    console.error("Flickフィードの取得エラー:", err);
  } finally {
    loadingFlicks.value = false;
  }
};

const handleFlickCreated = async () => {
  await fetchFlicks();
};

const handleFlickDeleted = (deletedFlickId: number) => {
  const index = flicks.value.findIndex((flick) => flick.id === deletedFlickId);
  if (index !== -1) {
    flicks.value.splice(index, 1);
  }
};

onMounted(() => {
  fetchFlicks();
});
</script>

<template>
  <main class="py-8">
    <div class="max-w-2xl mx-auto mb-8">
      <FlickCreateForm @flickCreated="handleFlickCreated" />
    </div>

    <h2 class="text-4xl font-bold text-center mb-10 text-purple-400 font-orbitron">Your Feed</h2>

    <LoadingStateContainer
      :loading="loadingFlicks"
      :error="errorFlicks"
      :is-empty="flicks.length === 0"
      loading-text="Flickを読み込み中..."
      empty-text="まだFlickがありません。誰かをフォローするか、Flickを投稿しましょう！"
    >
      <div class="space-y-8 max-w-2xl mx-auto">
        <FlickCard
          v-for="flick in flicks"
          :key="flick.id"
          :flick="flick"
          @flickDeleted="handleFlickDeleted"
        />
      </div>
    </LoadingStateContainer>
  </main>
</template>
