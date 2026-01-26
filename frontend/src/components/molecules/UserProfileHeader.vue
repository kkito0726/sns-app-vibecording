<script setup lang="ts">
import Avatar from "@/components/atoms/Avatar.vue";
import type { UserResponse } from "@/types/api";

interface Props {
  user: UserResponse;
  isMyProfile: boolean;
  isLoggedIn: boolean;
}

defineProps<Props>();

const emit = defineEmits<{
  toggleFollow: [];
}>();
</script>

<template>
  <div
    class="bg-gray-800/50 backdrop-blur-sm p-8 rounded-xl shadow-lg shadow-purple-500/20 border border-purple-500/30 mb-8 flex flex-col md:flex-row items-center space-y-6 md:space-y-0 md:space-x-8"
  >
    <Avatar
      :src="user.profileImageUrl"
      alt="Profile"
      size="xl"
      border-color="purple"
      border-width="thick"
      class="shadow-md"
    />
    <div class="text-center md:text-left">
      <h1 class="text-4xl font-bold text-purple-400 font-orbitron">
        {{ user.username }}
      </h1>
      <p class="text-gray-300 text-lg mt-2">{{ user.bio || "No bio available." }}</p>
      <div class="flex justify-center md:justify-start space-x-6 mt-4 text-gray-400">
        <p>
          <span class="font-bold text-purple-300">{{ user.followerCount }}</span>
          Followers
        </p>
        <p>
          <span class="font-bold text-purple-300">{{ user.followingCount }}</span>
          Following
        </p>
      </div>
      <div class="mt-6" v-if="!isMyProfile && isLoggedIn">
        <button
          @click="emit('toggleFollow')"
          :class="{
            'bg-pink-500 hover:bg-pink-600 shadow-pink-500/50': user.isFollowing,
            'bg-purple-500 hover:bg-purple-600 shadow-purple-500/50': !user.isFollowing,
          }"
          class="text-white font-bold py-3 px-6 rounded-lg shadow-lg transition-all duration-300 transform hover:scale-105"
        >
          {{ user.isFollowing ? "Unfollow" : "Follow" }}
        </button>
      </div>
    </div>
  </div>
</template>
