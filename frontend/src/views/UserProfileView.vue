<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import api from '@/api'
import FlickCard from '@/components/FlickCard.vue'
import type { UserResponse, FlickDetailResponse } from '@/types/api'

const route = useRoute()
const authStore = useAuthStore()

const userProfile = ref<UserResponse | null>(null)
const userFlicks = ref<FlickDetailResponse[]>([])
const isLoading = ref(true)
const error = ref<string | null>(null)

const userId = computed(() => parseInt(route.params.userId as string))
const isMyProfile = computed(() => authStore.user?.id === userId.value)
const isFollowing = computed(() => userProfile.value?.isFollowing ?? false)

const fetchUserProfile = async () => {
  try {
    isLoading.value = true
    error.value = null
    const { data } = await api.get<UserResponse>(`/users/${userId.value}`)
    userProfile.value = data
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Failed to fetch user profile.'
    userProfile.value = null
  }
}

const fetchUserFlicks = async () => {
  try {
    // APIにユーザーごとのFlickを取得するエンドポイントがないため、
    // 一旦全フィードを取得し、該当ユーザーのFlickのみをフィルタリング
    // TODO: 専用のAPIエンドポイントが用意されたら修正
    const { data } = await api.get<FlickDetailResponse[]>('/flicks/feed')
    userFlicks.value = data.filter(flick => flick.author.userId === userId.value)
  } catch (err) {
    console.error('Failed to fetch user flicks:', err)
    userFlicks.value = []
  }
}

const toggleFollow = async () => {
  if (!userProfile.value) return

  try {
    if (userProfile.value.isFollowing) {
      await api.delete(`/users/${userId.value}/follow`)
      userProfile.value.followerCount--
    } else {
      await api.post(`/users/${userId.value}/follow`)
      userProfile.value.followerCount++
    }
    userProfile.value.isFollowing = !userProfile.value.isFollowing
  } catch (err) {
    console.error('Failed to toggle follow:', err)
  }
}

onMounted(async () => {
  await fetchUserProfile()
  await fetchUserFlicks()
  isLoading.value = false
})

watch(
  () => route.params.userId,
  async (newUserId, oldUserId) => {
    if (newUserId !== oldUserId) {
      isLoading.value = true
      await fetchUserProfile()
      await fetchUserFlicks()
      isLoading.value = false
    }
  }
)
</script>

<template>
  <div class="user-profile-view py-8 px-4">
    <div v-if="isLoading" class="text-center text-purple-300 text-xl">Loading profile...</div>
    <div v-else-if="error" class="text-center text-pink-500 text-xl">{{ error }}</div>
    <div v-else-if="userProfile" class="max-w-2xl mx-auto">
      <div class="bg-gray-800/50 backdrop-blur-sm p-8 rounded-xl shadow-lg shadow-purple-500/20 border border-purple-500/30 mb-8 flex flex-col md:flex-row items-center space-y-6 md:space-y-0 md:space-x-8">
        <img
          :src="userProfile.profileImageUrl || 'https://via.placeholder.com/150'"
          alt="Profile"
          class="w-32 h-32 rounded-full object-cover border-4 border-purple-400 shadow-md"
        />
        <div class="text-center md:text-left">
          <h1 class="text-4xl font-bold text-purple-400 font-orbitron">{{ userProfile.username }}</h1>
          <p class="text-gray-300 text-lg mt-2">{{ userProfile.bio || 'No bio available.' }}</p>
          <div class="flex justify-center md:justify-start space-x-6 mt-4 text-gray-400">
            <p><span class="font-bold text-purple-300">{{ userProfile.followerCount }}</span> Followers</p>
            <p><span class="font-bold text-purple-300">{{ userProfile.followingCount }}</span> Following</p>
          </div>
          <div class="mt-6" v-if="!isMyProfile && authStore.isLoggedIn">
            <button
              @click="toggleFollow"
              :class="{
                'bg-pink-500 hover:bg-pink-600 shadow-pink-500/50': isFollowing,
                'bg-purple-500 hover:bg-purple-600 shadow-purple-500/50': !isFollowing
              }"
              class="text-white font-bold py-3 px-6 rounded-lg shadow-lg transition-all duration-300 transform hover:scale-105"
            >
              {{ isFollowing ? 'Unfollow' : 'Follow' }}
            </button>
          </div>
        </div>
      </div>

      <h2 class="text-3xl font-bold text-purple-400 font-orbitron mb-6 text-center">Flicks by {{ userProfile.username }}</h2>

      <div v-if="userFlicks.length > 0" class="space-y-8">
        <FlickCard v-for="flick in userFlicks" :key="flick.id" :flick="flick" />
      </div>
      <p v-else class="text-center text-gray-400 text-xl">This user has no flicks yet.</p>
    </div>
    <div v-else class="text-center text-gray-400 text-xl">No user profile found.</div>
  </div>
</template>
