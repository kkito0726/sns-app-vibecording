<script setup lang="ts">
import { ref, computed } from "vue";
import type { FlickDetailResponse, CommentResponse } from "@/types/api";
import api from "@/api";
import { RouterLink } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import Avatar from "@/components/atoms/Avatar.vue";
import ErrorAlert from "@/components/atoms/ErrorAlert.vue";
import LikeButton from "@/components/atoms/LikeButton.vue";
import CommentButton from "@/components/atoms/CommentButton.vue";
import CommentItem from "@/components/molecules/CommentItem.vue";
import CommentForm from "@/components/molecules/CommentForm.vue";

const props = defineProps<{
  flick: FlickDetailResponse;
}>();

const emit = defineEmits(["flickDeleted"]);

const authStore = useAuthStore();

const isLiked = ref(props.flick.isLiked);
const likeCount = ref(props.flick.likeCount);

const showComments = ref(false);
const comments = ref<CommentResponse[]>([]);
const commentLoading = ref(false);
const commentError = ref<string | null>(null);

const showMenu = ref(false);
const isMyFlick = computed(() => authStore.user?.id === props.flick.author.userId);

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

const postComment = async (text: string) => {
  commentLoading.value = true;
  commentError.value = null;
  try {
    await api.post(`/flicks/${props.flick.id}/comments`, { text });
    await fetchComments();
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
    emit("flickDeleted", props.flick.id);
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
    <!-- ヘッダー: アバター、ユーザー名、日時、メニュー -->
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

    <!-- コンテンツ: テキスト、画像、動画 -->
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

    <!-- アクションボタン -->
    <div class="flex items-center space-x-4 text-gray-400">
      <LikeButton :is-liked="isLiked" :count="likeCount" @click="toggleLike" />
      <CommentButton @click="toggleComments" />
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
        <CommentItem v-for="comment in comments" :key="comment.id" :comment="comment" />
      </div>

      <CommentForm :loading="commentLoading" @submit="postComment" class="mt-4" />
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
  opacity: 0;
}
</style>
