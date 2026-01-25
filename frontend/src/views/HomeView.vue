<script setup lang="ts">
import { ref, onMounted } from 'vue'
import api from '@/api'
import FlickCard from '@/components/FlickCard.vue'
import type { FlickDetailResponse } from '@/types/api'

const flicks = ref<FlickDetailResponse[]>([])
const loading = ref(true)
const error = ref<string | null>(null)

onMounted(async () => {
  try {
    const response = await api.get<FlickDetailResponse[]>('/flicks/feed')
    flicks.value = response.data
  } catch (err) {
    error.value = 'Failed to load flicks. Please try again later.'
    console.error('Error fetching flick feed:', err)
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <main class="py-8">
    <h1 class="text-4xl font-bold text-center mb-10 text-purple-400 font-orbitron">Your Feed</h1>

    <div v-if="loading" class="text-center text-purple-300 text-xl">Loading flicks...</div>
    <div v-else-if="error" class="text-center text-pink-500 text-xl">{{ error }}</div>
    <div v-else-if="flicks.length === 0" class="text-center text-gray-400 text-xl">No flicks yet. Follow some users or post your own!</div>
    <div v-else class="space-y-8 max-w-2xl mx-auto">
      <FlickCard v-for="flick in flicks" :key="flick.id" :flick="flick" />
    </div>
  </main>
</template>