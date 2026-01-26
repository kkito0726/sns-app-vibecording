<script setup lang="ts">
import { defineProps, ref, computed, defineEmits } from "vue";
import type { FlickDetailResponse, CommentResponse } from "@/types/api";
import api from "@/api";
import { RouterLink } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import Avatar from "@/components/atoms/Avatar.vue";
import ErrorAlert from "@/components/atoms/ErrorAlert.vue";

const props = defineProps<{
  flick: FlickDetailResponse;
}>();

const emit = defineEmits(["flickDeleted"]); // flick削除イベントを定義

const authStore = useAuthStore();

const isLiked = ref(props.flick.isLiked);
const likeCount = ref(props.flick.likeCount);

const showComments = ref(false);
const comments = ref<CommentResponse[]>([]);
const newCommentText = ref("");
const commentLoading = ref(false);
const commentError = ref<string | null>(null);

const showMenu = ref(false); // メニュー表示状態
const isMyFlick = computed(() => authStore.user?.id === props.flick.author.userId); // 自分のFlickかどうか

const toggleLike = async () => {
  try {
    if (isLiked.value) {
      await api.delete(`/flicks/${props.flick.id}/like`);
      likeCount.value--;
    } else {
      await api.post(`/flicks/${props.flick.id}/like`);
      likeCount.value++;
    }
    isLiked.value = !isLiked.value;
  } catch (error) {
    console.error("Failed to toggle like:", error);
  }
};

const fetchComments = async () => {
  commentLoading.value = true;
  commentError.value = null;
  try {
    const { data } = await api.get<CommentResponse[]>(`/flicks/${props.flick.id}/comments`);
    comments.value = data;
  } catch (error: any) {
    commentError.value = error.response?.data?.message || "コメントの読み込みに失敗しました。";
    console.error("Failed to fetch comments:", error);
  } finally {
    commentLoading.value = false;
  }
};

const postComment = async () => {
  if (!newCommentText.value.trim()) return;

  commentLoading.value = true;
  commentError.value = null;
  try {
    await api.post(`/flicks/${props.flick.id}/comments`, { text: newCommentText.value });
    newCommentText.value = "";
    await fetchComments(); // コメント投稿後に再取得
  } catch (error: any) {
    commentError.value = error.response?.data?.message || "コメントの投稿に失敗しました。";
    console.error("Failed to post comment:", error);
  } finally {
    commentLoading.value = false;
  }
};

const toggleComments = () => {
  showComments.value = !showComments.value;
  if (showComments.value && comments.value.length === 0) {
    fetchComments();
  }
};

const deleteFlick = async () => {
  if (!confirm("本当にこのFlickを削除しますか？")) {
    return;
  }
  try {
    await api.delete(`/flicks/${props.flick.id}`);
    emit("flickDeleted", props.flick.id); // 親コンポーネントに削除を通知
  } catch (error) {
    console.error("Failed to delete flick:", error);
    alert("Flickの削除に失敗しました。");
  }
};
</script>

<template>
  <div
    class="bg-gray-800/50 backdrop-blur-sm p-6 rounded-xl shadow-lg shadow-purple-500/20 border border-purple-500/30 mb-6 neon-border-animation"
  >
    <div class="flex items-center mb-4 relative">
      <Avatar
        :src="flick.author.profileImageUrl"
        alt="Profile"
        size="md"
        border-color="purple"
        class="mr-4"
      />
      <RouterLink
        :to="`/users/${flick.author.userId}`"
        class="text-purple-300 hover:text-purple-400 font-bold text-lg"
      >
        {{ flick.author.username }}
      </RouterLink>
      <div class="flex items-center ml-auto">
        <span class="text-gray-500 text-sm mr-2">{{
          new Date(flick.createdAt).toLocaleString()
        }}</span>

        <!-- 三点リーダーメニュー -->
        <div v-if="isMyFlick" class="relative">
          <button
            @click.stop="showMenu = !showMenu"
            class="p-1 rounded-full text-gray-400 hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-purple-500"
          >
            <svg
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              {/* @click.stop を追加 */}
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 5v.01M12 12v.01M12 19v.01M12 6a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2z"
              ></path>
            </svg>
          </button>

          <div
            v-if="showMenu"
            @click.away="showMenu = false"
            class="absolute right-0 mt-2 w-48 bg-gray-700 rounded-md shadow-lg z-10"
          >
            <button
              @click="deleteFlick"
              class="block w-full text-left px-4 py-2 text-sm text-red-400 hover:bg-gray-600"
            >
              削除
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="mb-4">
      <p v-if="flick.textContent" class="text-gray-100 text-lg mb-2">{{ flick.textContent }}</p>
      <img
        v-if="flick.imageUrl"
        :src="flick.imageUrl"
        alt="Flick image"
        class="rounded-lg w-full object-cover max-h-96 mb-2"
      />
      <video
        v-if="flick.videoUrl"
        :src="flick.videoUrl"
        controls
        class="rounded-lg w-full object-cover max-h-96 mb-2"
      ></video>
    </div>

    <div class="flex items-center space-x-4 text-gray-400">
      <button
        @click="toggleLike"
        class="flex items-center space-x-1 hover:text-pink-400 transition-colors duration-300"
      >
        <svg
          :class="{ 'text-pink-500': isLiked, 'text-gray-400': !isLiked }"
          class="w-6 h-6 fill-current transition-colors duration-300"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
        >
          <path
            d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
          />
        </svg>
        <span>{{ likeCount }}</span>
      </button>
      <button
        @click="toggleComments"
        class="flex items-center space-x-1 hover:text-cyan-400 transition-colors duration-300"
      >
        <svg class="w-6 h-6 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path
            d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z"
          />
        </svg>
        <span>コメント</span>
      </button>
    </div>

    <!-- コメントセクション -->
    <div v-if="showComments" class="mt-6 border-t border-gray-700 pt-4">
      <h3 class="text-xl font-bold text-gray-300 mb-4">コメント</h3>

      <div v-if="commentLoading" class="text-purple-300 text-sm">コメントを読み込み中...</div>
      <ErrorAlert v-else-if="commentError" :message="commentError" :centered="false" />
      <div v-else-if="comments.length === 0" class="text-gray-400 text-sm mb-4">
        まだコメントがありません。
      </div>
      <div v-else class="space-y-4 mb-4">
        <div
          v-for="comment in comments"
          :key="comment.id"
          class="bg-gray-700/30 p-3 rounded-lg flex items-start space-x-3"
        >
          <Avatar
            :src="comment.authorProfileImageUrl"
            alt="Author Profile"
            size="sm"
            border-color="purple"
            border-width="thin"
          />
          <div>
            <RouterLink
              :to="`/users/${comment.userId}`"
              class="font-bold text-purple-300 hover:text-purple-400 text-sm"
            >
              {{ comment.authorUsername }}
            </RouterLink>
            <p class="text-gray-200 text-sm" v-text="comment.text"></p>
            <span class="text-gray-500 text-xs">{{
              new Date(comment.createdAt).toLocaleString()
            }}</span>
          </div>
        </div>
      </div>

      <!-- コメント投稿フォーム -->
      <form @submit.prevent="postComment" class="flex items-center space-x-3 mt-4">
        <textarea
          v-model="newCommentText"
          rows="1"
          class="flex-grow px-3 py-2 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 placeholder-gray-500 focus:outline-none focus:ring-1 focus:ring-cyan-500 transition-all duration-300"
          placeholder="コメントを追加..."
        ></textarea>
        <button
          type="submit"
          :disabled="commentLoading || !newCommentText.trim()"
          class="bg-cyan-500 hover:bg-cyan-600 text-white font-bold py-2 px-4 rounded-lg shadow-lg shadow-cyan-500/50 transition-all duration-300 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          投稿
        </button>
      </form>
      <ErrorAlert v-if="commentError" :message="commentError" :centered="false" class="mt-2" />
    </div>
  </div>
</template>

<style scoped>
.neon-border-animation {
  position: relative;
  overflow: hidden;
}

.neon-border-animation::before {
  content: "";
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: linear-gradient(45deg, #ff00ff, #00ffff, #ff00ff);
  background-size: 400% 400%;
  z-index: -1;
  opacity: 0;
  transition: opacity 0.5s ease-in-out;
}

.neon-border-animation:hover::before {
  opacity: 0; /* ホバー時にアニメーションを非表示にする */
}
</style>
