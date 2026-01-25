<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const email = ref('')
const password = ref('')
const error = ref<string | null>(null)
const router = useRouter()
const authStore = useAuthStore()

const handleLogin = async () => {
  error.value = null
  try {
    await authStore.login({
      email: email.value,
      password: password.value,
    })
    router.push('/')
  } catch (err) {
    error.value = 'Login failed. Please check your credentials.'
    console.error('Login failed:', err)
  }
}
</script>

<template>
  <div class="max-w-md mx-auto mt-10 bg-gray-800/50 backdrop-blur-sm p-8 rounded-xl shadow-lg shadow-purple-500/20 border border-purple-500/30">
    <h1 class="text-4xl font-bold text-center mb-8 text-purple-400 font-orbitron">Login to Flick</h1>
    <form @submit.prevent="handleLogin" class="space-y-6">
      <div>
        <label for="email" class="block text-lg font-medium text-gray-300 mb-2">Email</label>
        <input
          id="email"
          v-model="email"
          type="email"
          required
          class="w-full px-4 py-3 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 focus:outline-none focus:ring-2 focus:ring-purple-500 transition-all duration-300"
          placeholder="you@example.com"
        />
      </div>
      <div>
        <label for="password" class="block text-lg font-medium text-gray-300 mb-2">Password</label>
        <input
          id="password"
          v-model="password"
          type="password"
          required
          class="w-full px-4 py-3 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 focus:outline-none focus:ring-2 focus:ring-purple-500 transition-all duration-300"
          placeholder="********"
        />
      </div>
      <div v-if="error" class="text-pink-500 text-center">
        {{ error }}
      </div>
      <div>
        <button
          type="submit"
          class="w-full bg-purple-500 hover:bg-purple-600 text-white font-bold py-3 px-4 rounded-lg shadow-lg shadow-purple-500/50 transition-all duration-300 transform hover:scale-105"
        >
          Login
        </button>
      </div>
    </form>
     <div class="text-center mt-4">
      <RouterLink to="/register" class="text-gray-400 hover:text-purple-400">
        Don't have an account? Register here.
      </RouterLink>
    </div>
  </div>
</template>