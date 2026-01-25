<script setup lang="ts">
import { defineProps, ref } from 'vue'
import type { FlickDetailResponse } from '@/types/api'
import api from '@/api'
import { RouterLink } from 'vue-router'

const props = defineProps<{
  flick: FlickDetailResponse
}>()

const isLiked = ref(props.flick.isLiked)
const likeCount = ref(props.flick.likeCount)

const toggleLike = async () => {
  try {
    if (isLiked.value) {
      await api.delete(`/flicks/${props.flick.id}/like`)
      likeCount.value--
    } else {
      await api.post(`/flicks/${props.flick.id}/like`)
      likeCount.value++
    }
    isLiked.value = !isLiked.value
  } catch (error) {
    console.error('Failed to toggle like:', error)
  }
}
</script>

<template>
  <div class="bg-gray-800/50 backdrop-blur-sm p-6 rounded-xl shadow-lg shadow-purple-500/20 border border-purple-500/30 mb-6 neon-border-animation">
    <div class="flex items-center mb-4">
      <img
        :src="flick.author.profileImageUrl || 'https://via.placeholder.com/40'"
        alt="Profile"
        class="w-10 h-10 rounded-full mr-4 border-2 border-purple-400"
      />
      <RouterLink :to="`/users/${flick.author.userId}`" class="text-purple-300 hover:text-purple-400 font-bold text-lg">
        {{ flick.author.username }}
      </RouterLink>
      <span class="text-gray-500 text-sm ml-auto">{{ new Date(flick.createdAt).toLocaleString() }}</span>
    </div>

    <div class="mb-4">
      <p v-if="flick.textContent" class="text-gray-100 text-lg mb-2">{{ flick.textContent }}</p>
      <img v-if="flick.imageUrl" :src="flick.imageUrl" alt="Flick image" class="rounded-lg w-full object-cover max-h-96 mb-2" />
      <video v-if="flick.videoUrl" :src="flick.videoUrl" controls class="rounded-lg w-full object-cover max-h-96 mb-2"></video>
    </div>

    <div class="flex items-center space-x-4 text-gray-400">
      <button @click="toggleLike" class="flex items-center space-x-1 hover:text-pink-400 transition-colors duration-300">
        <svg
          :class="{ 'text-pink-500': isLiked, 'text-gray-400': !isLiked }"
          class="w-6 h-6 fill-current transition-colors duration-300"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
        >
          <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
        </svg>
        <span>{{ likeCount }}</span>
      </button>
      <!-- コメントボタンは後で実装 -->
      <button class="flex items-center space-x-1 hover:text-blue-400 transition-colors duration-300">
        <svg class="w-6 h-6 fill-current" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z"/>
        </svg>
        <span>コメント</span>
      </button>
    </div>
  </div>
</template>

<style scoped>
.neon-border-animation {
  position: relative;
  overflow: hidden;
}

.neon-border-animation::before {
  content: '';
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
  opacity: 1;
  animation: neon-glow 1.5s linear infinite;
}

@keyframes neon-glow {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}
</style>
