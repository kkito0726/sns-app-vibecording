<script setup lang="ts">
import { ref } from "vue";
import api from "@/api";
import type { FlickResponse } from "@/types/api";
import ErrorAlert from "@/components/atoms/ErrorAlert.vue";
import FileUploadButton from "@/components/atoms/FileUploadButton.vue";

const emit = defineEmits<{
  flickCreated: [];
}>();

const textContent = ref("");
const imageFile = ref<File | null>(null);
const videoFile = ref<File | null>(null);
const postType = ref<"TEXT" | "IMAGE" | "VIDEO">("TEXT");
const errorCreateFlick = ref<string | null>(null);
const loadingCreateFlick = ref(false);

const imageUploadRef = ref<InstanceType<typeof FileUploadButton> | null>(null);
const videoUploadRef = ref<InstanceType<typeof FileUploadButton> | null>(null);

const onImageChange = (file: File | null) => {
  if (file) {
    imageFile.value = file;
    videoFile.value = null;
    videoUploadRef.value?.reset();
    postType.value = "IMAGE";
  } else {
    imageFile.value = null;
    if (!videoFile.value && !textContent.value) {
      postType.value = "TEXT";
    }
  }
};

const onVideoChange = (file: File | null) => {
  if (file) {
    videoFile.value = file;
    imageFile.value = null;
    imageUploadRef.value?.reset();
    postType.value = "VIDEO";
  } else {
    videoFile.value = null;
    if (!imageFile.value && !textContent.value) {
      postType.value = "TEXT";
    }
  }
};

const handleTextChange = () => {
  if (textContent.value || imageFile.value || videoFile.value) {
    if (videoFile.value) postType.value = "VIDEO";
    else if (imageFile.value) postType.value = "IMAGE";
    else postType.value = "TEXT";
  } else {
    postType.value = "TEXT";
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
    const currentPostType = (videoFile.value ? "VIDEO" : imageFile.value ? "IMAGE" : "TEXT") as
      | "TEXT"
      | "IMAGE"
      | "VIDEO";

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

    emit("flickCreated");

    // フォームをリセット
    textContent.value = "";
    imageFile.value = null;
    videoFile.value = null;
    postType.value = "TEXT";
    imageUploadRef.value?.reset();
    videoUploadRef.value?.reset();
  } catch (err: any) {
    errorCreateFlick.value = err.response?.data?.message || "Flickの投稿に失敗しました。";
    console.error("Flickの投稿エラー:", err);
  } finally {
    loadingCreateFlick.value = false;
  }
};
</script>

<template>
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
          <FileUploadButton
            ref="imageUploadRef"
            accept="image/*"
            color="purple"
            title="画像をアップロード"
            @change="onImageChange"
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
          </FileUploadButton>

          <FileUploadButton
            ref="videoUploadRef"
            accept="video/*"
            color="pink"
            title="動画をアップロード"
            @change="onVideoChange"
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
          </FileUploadButton>
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

      <!-- エラー表示 -->
      <ErrorAlert :message="errorCreateFlick" :centered="false" class="mt-2" />
    </form>
  </div>
</template>
