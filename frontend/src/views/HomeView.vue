<script setup lang="ts">
import { ref, onMounted } from "vue";
import api from "@/api";
import FlickCard from "@/components/FlickCard.vue";
import type { FlickDetailResponse, FlickResponse } from "@/types/api";

// HomeView の状態
const flicks = ref<FlickDetailResponse[]>([]);
const loadingFlicks = ref(true);
const errorFlicks = ref<string | null>(null);

// CreateFlickView の状態 (統合)
const textContent = ref("");
const imageFile = ref<File | null>(null);
const videoFile = ref<File | null>(null);
const postType = ref<"TEXT" | "IMAGE" | "VIDEO">("TEXT");
const errorCreateFlick = ref<string | null>(null);
const loadingCreateFlick = ref(false);

// ファイル入力要素への参照
const imageInputRef = ref<HTMLInputElement | null>(null);
const videoInputRef = ref<HTMLInputElement | null>(null);

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

const onImageChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    imageFile.value = target.files[0];
    videoFile.value = null; // 画像が選択されたら動画をクリア
    if (videoInputRef.value) videoInputRef.value.value = "";
    postType.value = "IMAGE";
  } else {
    imageFile.value = null;
    if (!videoFile.value && !textContent.value) {
      // テキストも画像も動画もなければTEXT
      postType.value = "TEXT";
    }
  }
};

const onVideoChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    videoFile.value = target.files[0];
    imageFile.value = null; // 動画が選択されたら画像をクリア
    if (imageInputRef.value) imageInputRef.value.value = "";
    postType.value = "VIDEO";
  } else {
    videoFile.value = null;
    if (!imageFile.value && !textContent.value) {
      // テキストも画像も動画もなければTEXT
      postType.value = "TEXT";
    }
  }
};

const handleTextChange = () => {
  if (textContent.value || imageFile.value || videoFile.value) {
    // 優先順位: VIDEO > IMAGE > TEXT
    if (videoFile.value) postType.value = "VIDEO";
    else if (imageFile.value) postType.value = "IMAGE";
    else postType.value = "TEXT";
  } else {
    postType.value = "TEXT"; // 何も入力されていない場合はデフォルトでTEXT
  }
};

const createFlick = async () => {
  errorCreateFlick.value = null;
  loadingCreateFlick.value = true;

  const formData = new FormData();
  if (textContent.value) {
    formData.append("textContent", textContent.value);
  }
  if (imageFile.value) {
    formData.append("imageFile", imageFile.value);
  }
  if (videoFile.value) {
    formData.append("videoFile", videoFile.value);
  }

  try {
    // postTypeが正しく設定されていることを確認
    const currentPostType = (videoFile.value ? "VIDEO" : imageFile.value ? "IMAGE" : "TEXT") as
      | "TEXT"
      | "IMAGE"
      | "VIDEO";

    // textContentがない場合にTEXTタイプで投稿されるのを防ぐ
    if (currentPostType === "TEXT" && !textContent.value.trim()) {
      errorCreateFlick.value = "テキスト内容がありません。";
      loadingCreateFlick.value = false;
      return;
    }

    await api.post<FlickResponse>(`/flicks?postType=${currentPostType}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    console.log("Flick created successfully");

    // 新しいFlickをフィードの先頭に追加するためにフィードを再取得
    await fetchFlicks();

    // フォームをリセット
    textContent.value = "";
    imageFile.value = null;
    videoFile.value = null;
    postType.value = "TEXT"; // リセット後にデフォルトのポストタイプをTEXTに戻す
    if (imageInputRef.value) imageInputRef.value.value = "";
    if (videoInputRef.value) videoInputRef.value.value = "";
  } catch (err: any) {
    errorCreateFlick.value = err.response?.data?.message || "Flickの投稿に失敗しました。";
    console.error("Flickの投稿エラー:", err);
  } finally {
    loadingCreateFlick.value = false;
  }
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
      <div
        class="bg-gray-800/50 backdrop-blur-sm p-8 rounded-xl shadow-lg shadow-purple-500/20 border border-purple-500/30"
      >
        <h1 class="text-4xl font-bold text-center mb-8 text-cyan-400 font-orbitron">
          新しいFlickを投稿
        </h1>

        <form @submit.prevent="createFlick" class="space-y-2">
          <div>
            <label for="textContent" class="sr-only">何かつぶやこう！</label>
            <textarea
              id="textContent"
              v-model="textContent"
              @input="handleTextChange"
              rows="4"
              class="w-full px-4 py-3 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-purple-500 transition-all duration-300"
              placeholder="今日の出来事、感じたこと..."
            ></textarea>
          </div>

          <!-- ファイルアップロードアイコンと投稿ボタン -->
          <div class="flex items-center justify-between mt-4">
            <div class="flex space-x-4">
              <input
                type="file"
                id="imageUpload"
                accept="image/*"
                @change="onImageChange"
                class="hidden"
                ref="imageInputRef"
              />
              <button
                type="button"
                @click="imageInputRef?.click()"
                class="flex items-center justify-center p-2 rounded-full bg-purple-500 hover:bg-purple-600 text-white shadow-lg shadow-purple-500/50 transition-all duration-300 transform hover:scale-110"
                title="画像をアップロード"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-6 w-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                  stroke-width="2"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"
                  />
                </svg>
              </button>

              <input
                type="file"
                id="videoUpload"
                accept="video/*"
                @change="onVideoChange"
                class="hidden"
                ref="videoInputRef"
              />
              <button
                type="button"
                @click="videoInputRef?.click()"
                class="flex items-center justify-center p-2 rounded-full bg-pink-500 hover:bg-pink-600 text-white shadow-lg shadow-pink-500/50 transition-all duration-300 transform hover:scale-110"
                title="動画をアップロード"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-5 w-5"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                  stroke-width="2"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M15 10l4.553-2.276A1 1 0 0121 8.618v6.764a1 1 0 01-1.447.894L15 14M4 18h16a2 2 0 002-2V8a2 2 0 00-2-2H4a2 2 0 00-2 2v8a2 2 0 002 2z"
                  />
                </svg>
              </button>
            </div>

            <button
              type="submit"
              :disabled="loadingCreateFlick || (!textContent && !imageFile && !videoFile)"
              class="bg-gradient-to-r from-cyan-500 to-blue-500 hover:from-cyan-600 hover:to-blue-600 text-white font-bold py-2 px-4 rounded-lg shadow-lg shadow-cyan-500/50 transition-all duration-300 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed w-auto"
            >
              <span v-if="loadingCreateFlick">投稿中...</span>
              <span v-else>Flickを投稿</span>
            </button>
          </div>

          <!-- ファイル名表示 -->
          <div class="flex justify-end space-x-4 text-gray-400 text-sm mt-2">
            <p v-if="imageFile" class="flex-grow text-right pr-4">画像: {{ imageFile.name }}</p>
            <p v-else class="flex-grow text-right pr-4"></p>
            <p v-if="videoFile" class="flex-grow text-right">動画: {{ videoFile.name }}</p>
            <p v-else class="flex-grow text-right"></p>
          </div>
        </form>
      </div>
    </div>

    <h2 class="text-4xl font-bold text-center mb-10 text-purple-400 font-orbitron">Your Feed</h2>

    <div v-if="loadingFlicks" class="text-center text-purple-300 text-xl">Flickを読み込み中...</div>
    <div v-else-if="errorFlicks" class="text-center text-pink-500 text-xl">{{ errorFlicks }}</div>
    <div v-else-if="flicks.length === 0" class="text-center text-gray-400 text-xl">
      まだFlickがありません。誰かをフォローするか、Flickを投稿しましょう！
    </div>
    <div v-else class="space-y-8 max-w-2xl mx-auto">
      <FlickCard v-for="flick in flicks" :key="flick.id" :flick="flick" @flickDeleted="handleFlickDeleted" />
    </div>
  </main>
</template>
