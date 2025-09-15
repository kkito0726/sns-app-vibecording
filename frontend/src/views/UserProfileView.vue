<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { useFlickStore } from "@/stores/flick"; // Flickストアをインポート
import axios from "axios";

const route = useRoute();
const authStore = useAuthStore();
const flickStore = useFlickStore(); // Flickストアを使用

const username = ref<string | string[]>(route.params.username);
const userProfile = ref<any>(null);
const isLoading = ref(true);
const error = ref<string | null>(null);

const isMyProfile = computed(() => {
  // authStore.user が存在しないため、一旦コメントアウト
  // return authStore.user?.username === username.value
  return false;
});

const fetchUserProfile = async () => {
  try {
    isLoading.value = true;
    error.value = null;
    const response = await axios.get(`/api/users/${username.value}`, {
      headers: {
        Authorization: `Bearer ${authStore.token}`,
      },
    });
    userProfile.value = response.data;
  } catch (err: any) {
    error.value = err.response?.data?.message || "Failed to fetch user profile.";
  } finally {
    isLoading.value = false;
  }
};

const followUser = async () => {
  try {
    await axios.post(
      `/api/users/${username.value}/follow`,
      {},
      {
        headers: {
          Authorization: `Bearer ${authStore.token}`,
        },
      }
    );
    // フォロー成功後、プロフィールを再取得してUIを更新
    await fetchUserProfile();
  } catch (err: any) {
    error.value = err.response?.data?.message || "Failed to follow user.";
  }
};

const unfollowUser = async () => {
  try {
    await axios.delete(`/api/users/${username.value}/follow`, {
      headers: {
        Authorization: `Bearer ${authStore.token}`,
      },
    });
    // アンフォロー成功後、プロフィールを再取得してUIを更新
    await fetchUserProfile();
  } catch (err: any) {
    error.value = err.response?.data?.message || "Failed to unfollow user.";
  }
};

onMounted(async () => {
  await fetchUserProfile();
  // ユーザーのFlick一覧を取得
  if (typeof username.value === "string") {
    await flickStore.fetchUserFlicks(username.value);
  }
});

const toggleLike = (flickId: number) => {
  const flick = flickStore.flicks.find((f) => f.id === flickId);
  if (flick) {
    if (flick.isLiked) {
      flickStore.unlike(flickId);
    } else {
      flickStore.like(flickId);
    }
  }
};
</script>

<template>
  <div class="user-profile-view p-4">
    <h1 class="text-3xl font-bold mb-4">User Profile: {{ username }}</h1>

    <div v-if="isLoading" class="text-gray-600">Loading profile...</div>
    <div v-else-if="error" class="text-red-500">{{ error }}</div>
    <div v-else-if="userProfile">
      <div class="flex items-center space-x-4 mb-4">
        <img
          :src="userProfile.profileImageUrl || 'https://via.placeholder.com/150'"
          alt="Profile"
          class="w-24 h-24 rounded-full object-cover"
        />
        <div>
          <p class="text-xl font-semibold">{{ userProfile.username }}</p>
          <p class="text-gray-600">{{ userProfile.bio || "No bio available." }}</p>
          <div class="flex space-x-4 mt-2">
            <p>Followers: {{ userProfile.followerCount }}</p>
            <p>Following: {{ userProfile.followingCount }}</p>
          </div>
        </div>
      </div>

      <div v-if="!isMyProfile" class="mt-4">
        <button
          v-if="!userProfile.isFollowing"
          @click="followUser"
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          Follow
        </button>
        <button
          v-else
          @click="unfollowUser"
          class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
        >
          Unfollow
        </button>
      </div>

      <h2 class="text-2xl font-bold mt-8 mb-4">Flicks by {{ username }}</h2>

      <div
        v-if="flickStore.flicks.length > 0"
        class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4"
      >
        <div
          v-for="flick in flickStore.flicks"
          :key="flick.id"
          class="border rounded-lg p-4 shadow"
        >
          <p v-if="flick.textContent">{{ flick.textContent }}</p>
          <img
            v-if="flick.imageUrl"
            :src="flick.imageUrl"
            alt="Flick image"
            class="mt-2 rounded-lg w-full"
          />

          <div class="flex items-center justify-between mt-4 text-sm text-gray-600">
            <span>{{ new Date(flick.createdAt).toLocaleString() }}</span>
            <div class="flex items-center space-x-2">
              <button @click="toggleLike(flick.id)" class="focus:outline-none">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-6 w-6"
                  :class="{
                    'text-red-500 fill-current': flick.isLiked,
                    'text-gray-400': !flick.isLiked,
                  }"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M4.318 6.318a4.5 4.5 0 016.364 0L12 7.636l1.318-1.318a4.5 4.5 0 016.364 6.364L12 20.364l-7.682-7.682a4.5 4.5 0 010-6.364z"
                  />
                </svg>
              </button>
              <span>{{ flick.likes }}</span>
            </div>
          </div>
        </div>
      </div>
      <p v-else>This user has no flicks yet.</p>
    </div>
    <div v-else class="text-gray-600">No user profile found.</div>
  </div>
</template>

<style scoped>
/* 必要に応じてスタイルを追加 */
</style>
