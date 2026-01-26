<script setup lang="ts">
import { ref, onMounted, computed, watch } from "vue";
import { useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import api from "@/api";
import FlickCard from "@/components/organisms/FlickCard.vue";
import LoadingStateContainer from "@/components/molecules/LoadingStateContainer.vue";
import UserProfileHeader from "@/components/molecules/UserProfileHeader.vue";
import type { UserResponse, FlickDetailResponse } from "@/types/api";

const route = useRoute();
const authStore = useAuthStore();

const userProfile = ref<UserResponse | null>(null);
const userFlicks = ref<FlickDetailResponse[]>([]);
const isLoading = ref(true);
const error = ref<string | null>(null);

const userId = computed(() => parseInt(route.params.userId as string));
const isMyProfile = computed(() => authStore.user?.id === userId.value);

const fetchUserProfile = async () => {
  try {
    isLoading.value = true;
    error.value = null;
    const { data } = await api.get<UserResponse>(`/users/${userId.value}`);
    userProfile.value = data;
  } catch (err: any) {
    error.value = err.response?.data?.message || "Failed to fetch user profile.";
    userProfile.value = null;
  }
};

const fetchUserFlicks = async () => {
  try {
    const { data } = await api.get<FlickDetailResponse[]>("/flicks/feed");
    userFlicks.value = data.filter((flick) => flick.author.userId === userId.value);
  } catch (err) {
    console.error("Failed to fetch user flicks:", err);
    userFlicks.value = [];
  }
};

const toggleFollow = async () => {
  if (!userProfile.value) return;

  try {
    if (userProfile.value.isFollowing) {
      await api.delete(`/users/${userId.value}/follow`);
      userProfile.value.followerCount--;
    } else {
      await api.post(`/users/${userId.value}/follow`);
      userProfile.value.followerCount++;
    }
    userProfile.value.isFollowing = !userProfile.value.isFollowing;
  } catch (err) {
    console.error("Failed to toggle follow:", err);
  }
};

const handleFlickDeleted = (deletedFlickId: number) => {
  const index = userFlicks.value.findIndex((flick) => flick.id === deletedFlickId);
  if (index !== -1) {
    userFlicks.value.splice(index, 1);
  }
};

onMounted(async () => {
  await fetchUserProfile();
  await fetchUserFlicks();
  isLoading.value = false;
});

watch(
  () => route.params.userId,
  async (newUserId, oldUserId) => {
    if (newUserId !== oldUserId) {
      isLoading.value = true;
      await fetchUserProfile();
      await fetchUserFlicks();
      isLoading.value = false;
    }
  },
);
</script>

<template>
  <div class="user-profile-view py-8 px-4">
    <LoadingStateContainer
      :loading="isLoading"
      :error="error"
      :is-empty="!userProfile"
      loading-text="Loading profile..."
      empty-text="No user profile found."
    >
      <div v-if="userProfile" class="max-w-2xl mx-auto">
        <UserProfileHeader
          :user="userProfile"
          :is-my-profile="isMyProfile"
          :is-logged-in="authStore.isLoggedIn"
          @toggle-follow="toggleFollow"
        />

        <h2 class="text-3xl font-bold text-purple-400 font-orbitron mb-6 text-center">
          Flicks by {{ userProfile.username }}
        </h2>

        <div v-if="userFlicks.length > 0" class="space-y-8">
          <FlickCard
            v-for="flick in userFlicks"
            :key="flick.id"
            :flick="flick"
            @flickDeleted="handleFlickDeleted"
          />
        </div>
        <p v-else class="text-center text-gray-400 text-xl">This user has no flicks yet.</p>
      </div>
    </LoadingStateContainer>
  </div>
</template>
