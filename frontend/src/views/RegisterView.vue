<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const username = ref('')
const email = ref('')
const password = ref('')
const error = ref<string | null>(null)
const router = useRouter()
const authStore = useAuthStore()

const handleRegister = async () => {
  error.value = null
  try {
    await authStore.register({
      username: username.value,
      email: email.value,
      password: password.value,
    })
    router.push('/login')
  } catch (err) {
    error.value = 'Registration failed. Please try again.'
    console.error('Registration failed:', err)
  }
}
</script>

<template>
  <div class="max-w-md mx-auto mt-10 bg-gray-800/50 backdrop-blur-sm p-8 rounded-xl shadow-lg shadow-pink-500/20 border border-pink-500/30">
    <h1 class="text-4xl font-bold text-center mb-8 text-pink-400 font-orbitron">Create Flick Account</h1>
    <form @submit.prevent="handleRegister" class="space-y-6">
      <div>
        <label for="username" class="block text-lg font-medium text-gray-300 mb-2">Username</label>
        <input
          id="username"
          v-model="username"
          type="text"
          required
          class="w-full px-4 py-3 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 focus:outline-none focus:ring-2 focus:ring-pink-500 transition-all duration-300"
          placeholder="yourusername"
        />
      </div>
      <div>
        <label for="email" class="block text-lg font-medium text-gray-300 mb-2">Email</label>
        <input
          id="email"
          v-model="email"
          type="email"
          required
          class="w-full px-4 py-3 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 focus:outline-none focus:ring-2 focus:ring-pink-500 transition-all duration-300"
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
          class="w-full px-4 py-3 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 focus:outline-none focus:ring-2 focus:ring-pink-500 transition-all duration-300"
          placeholder="********"
        />
      </div>
      <div v-if="error" class="text-red-500 text-center">
        {{ error }}
      </div>
      <div>
        <button
          type="submit"
          class="w-full bg-pink-500 hover:bg-pink-600 text-white font-bold py-3 px-4 rounded-lg shadow-lg shadow-pink-500/50 transition-all duration-300 transform hover:scale-105"
        >
          Register
        </button>
      </div>
    </form>
    <div class="text-center mt-4">
      <RouterLink to="/login" class="text-gray-400 hover:text-pink-400">
        Already have an account? Login here.
      </RouterLink>
    </div>
  </div>
</template>